package com.miniProj02.ayo.board;

import com.miniProj02.ayo.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardMapper boardMapper;

    // 첨부 파일을 저장할 경로
    private final String CURR_IMAGE_REPO_PATH = "c:\\upload-mini2";
    private final BoardFileMapper boardFileMapper;
    private final BoardTokenMapper boardTokenMapper;
    private final BoardImageFileMapper boardImageFileMapper;

    // 날짜 서식을 생성한다
    // File.separator는 /폴더명/폴더명/ 이때, '/'를 의미한다.
    private final SimpleDateFormat DATA_FORMAT =
            new SimpleDateFormat(File.separator + "YYYY" + File.separator + "MM" + File.separator + "dd");
    // ex) /2024/04/15 이렇게 폴더 구조가 생성된다.

    public PageResponseVO<BoardVO> getList(PageRequestVO pageRequestVO) {
        List<BoardVO> list = null;
        PageResponseVO<BoardVO> pageResponseVO = null;
        int total = 0;

        list = boardMapper.getList(pageRequestVO);
        total = boardMapper.getTotalCount(pageRequestVO);

        pageResponseVO = new PageResponseVO<>(list, total, pageRequestVO.getPageNo(), pageRequestVO.getSize());
        return pageResponseVO;
    }

    @Transactional
    public BoardVO view(BoardVO boardVO, Authentication authentication) {
        // 로그인한 세션 계정
        MemberVO loginMember = (MemberVO) authentication.getPrincipal();
        if (!boardVO.getAuthor().equals(loginMember.getId())) {
            // 동일하지않다면 view_count 증가
            boardMapper.increaseViewCount(boardVO);
        }
        BoardVO findBoardVO = boardMapper.view(boardVO);
        // 첨부파일도 가져오기
        findBoardVO.setBoardFileVO(boardFileMapper.getFile(boardVO));
        return findBoardVO;
    }

    @Transactional
    public BoardVO getBoard(BoardVO boardVO){
        return boardMapper.view(boardVO);
    }

    public BoardFileVO getBoardFile(Long id) {
        return boardFileMapper.getFile(BoardVO.builder().id(id).build());
    }

    public BoardVO checkPassword(BoardVO boardVO) {
        return boardMapper.checkPassword(boardVO);
    }

    public BoardVO fetchUpdateData(BoardVO boardVO) {
        BoardVO findBoardVO = boardMapper.view(boardVO);
        // 첨부파일도 가져오기
        findBoardVO.setBoardFileVO(boardFileMapper.getFile(boardVO));

        return findBoardVO;
    }

    @Transactional
    public int delete(BoardVO boardVO) {
        return boardMapper.delete(boardVO);
    }

    @Transactional
    public int update(BoardVO boardVO) {
        int updated = boardMapper.update(boardVO); // board 업데이트

        // 해당 게시글과 연관된 파일 정보 가져오기 - 여기에 진짜 파일 경로가 있음
        MultipartFile newFile = boardVO.getFile();
        BoardFileVO prevFile = boardFileMapper.getFile(boardVO);

        // 파일 요청이 온 경우
        if (newFile != null && newFile.getSize() != 0) {

            // 기존 파일이 존재한다면 기존 파일 삭제
            if(prevFile != null){
                int deleteFile = boardFileMapper.delete(prevFile); // DB 삭제
                if (deleteFile == 1) deleteFile(prevFile); // 물리적 삭제
            }

            // 새 파일 등록
            BoardFileVO boardFileVO = writeFile(boardVO.getFile());
            if (boardFileVO != null) {
                // 첨부파일에 게시물의 아이디를 설정한다
                boardFileVO.setBoard_id(boardVO.getId());
                // 파일 정보를 DB에 저장한다
                boardFileMapper.insert(boardFileVO);
            }
        }

        String content = boardVO.getContent(); // 비교를 위한 게시글 내용
        final String imageURL = "/board/image/";

        // 1. token의 값에 대한 전체 이미지 목록
        List<BoardImageFileVO> boardImageFiles = boardImageFileMapper.getBoardImages(boardVO.getToken());
        // 1. 게시글 id 값에 대한 전체 이미지 목록
        List<BoardImageFileVO> boardImageFilesByBoardId = boardImageFileMapper.getBoardImageFileListByBoardId(boardVO.getId());

        // 검사할 이미지 파일 리스트를 합치기
        boardImageFiles.addAll(boardImageFilesByBoardId);

        //2. 게시물 내용 중 이미지가 사용중이 아니면 삭제 목록에 추가
        List<BoardImageFileVO> deleteImageList = boardImageFiles.stream().filter(
                //게시물 내용에 해당 이미지가 존재하지 않으면 삭제 대상
                fileUpload -> !content.contains(imageURL + fileUpload.getId())
        ).collect(Collectors.toList());

        if (deleteImageList.size() != 0) {
            //3. 삭제 목록에 있는 이미지를 (파일)삭제 한다
            deleteImageList.stream().forEach(boardImageFile -> new File(boardImageFile.getReal_filename()).delete());
            deleteImageList.stream().forEach((file) -> deleteFile(file));

            //3. 삭제 목록에 있는 이미지를 (DB)삭제 한다
            Map<String, Object> map = new HashMap<>();
            map.put("list", deleteImageList);
            boardImageFileMapper.deleteBoardImageFiles(map);
        }

        return updated;
    }

    @Transactional
    public int insert(BoardVO boardVO) {
        int updated = boardMapper.insert(boardVO);

        // MultipartFile 객체를 파일에 저장한다. - 실제로 저장소에 저장하는 행위이다.
        // - 만약 클라우드라면 클라우드 저장소에
        // - 로컬이라면 로컬 디렉토리에
        BoardFileVO boardFileVO = writeFile(boardVO.getFile()); // private MultipartFile file;

        if (boardFileVO != null) {
            // 첨부파일에 게시물의 아이디를 설정한다
            boardFileVO.setBoard_id(boardVO.getId());

            // 파일 정보를 DB에 저장한다
            boardFileMapper.insert(boardFileVO);
        }

        // 게시물 등록 시, 사용한 임시 토큰들의 상태를 0:사용중 -> 1: 사용 완료 상태로 변경한다.
        boardTokenMapper.updateState(boardVO.getToken());

        // 실제 게시글 내용에 사용된 이미지만 게시물 id와 연결시킨다.
        String content = boardVO.getContent(); // 비교를 위한 게시글 내용
        final String imageURL = "/board/image/";

        // 1. token의 값에 대한 전체 이미지 목록
        List<BoardImageFileVO> boardImageFiles = boardImageFileMapper.getBoardImages(boardVO.getToken());

        //2. 게시물 내용 중 이미지가 사용중이 아니면 삭제 목록에 추가
        List<BoardImageFileVO> deleteImageList = boardImageFiles.stream().filter(
                //게시물 내용에 해당 이미지가 존재하지 않으면 삭제 대상
                fileUpload -> !content.contains(imageURL + fileUpload.getId())
        ).collect(Collectors.toList());

        if (deleteImageList.size() != 0) {
            //3. 삭제 목록에 있는 이미지를 (파일)삭제 한다
            deleteImageList.stream().forEach(boardImageFile -> new File(boardImageFile.getReal_filename()).delete());
            deleteImageList.stream().forEach((file) -> deleteFile(file));

            //3. 삭제 목록에 있는 이미지를 (DB)삭제 한다
            Map<String, Object> map = new HashMap<>();
            map.put("list", deleteImageList);
            boardImageFileMapper.deleteBoardImageFiles(map);
        }

        //4. 게시물 이미지의 board_token 값을 bno로 변경한다
        boardImageFileMapper.updateImageBoardId(boardVO);

        return updated;
    }

    /**
     * MultipartFile 객체를 실제로 물리적인 공간에 저장하는 메서드
     */
    private BoardFileVO writeFile(MultipartFile file) {
        if (file == null || file.getName() == null || file.getSize() == 0) return null;

        Calendar now = Calendar.getInstance();
        // 저장위치를 오늘의 날짜를 사용해 만든다. format은 위에 설정해놓았다.
        // 년도/월/날짜 구조를 가진 디렉터리 내부에 저장한다.
        String realFolder = DATA_FORMAT.format(now.getTime());

        // 실제 저장 위치를 생성한다.
        // ex)  "c:\\upload-mini\2024\4\15"가 된다.
        File realPath = new File(CURR_IMAGE_REPO_PATH + realFolder);

        //오늘 날짜에 대한 폴더가 없으면 생성한다.
        if (!realPath.exists()) {
            realPath.mkdirs();
        }

        // 실제 파일명으로 사용할 이름을 생성한다
        String fileNameReal = UUID.randomUUID().toString(); // 고유한 파일명을 위해 UUID를 사용
        // 파일 실제 경로와 파일 이름을 사용해 File 객체를 생성한다.
        File realFile = new File(realPath, fileNameReal); // (파일경로, 파일명)

        // 파일을 실제 위치에 저장한다
        try {
            // 여기서 file이 실제 파일 정보가 들어있는 객체이다.
            // realFile은 파일의 위치이다.
            file.transferTo(realFile);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("transferTo : {}", e);
            return null;
        }

        // 저장된 첨부파일 객체를 리턴한다
        return BoardFileVO.builder()
                .content_type(file.getContentType()) // 메서드들은 모두 file api에서 제공함
                .original_filename(file.getOriginalFilename())
                .real_filename(realFile.getAbsolutePath())
                .size(file.getSize())
                .build();
    }

    /**
     * 물리적인 저장소에서 해당 파일 삭제하기
     */
    private void deleteFile(FileVO file) {
        // 파일의 실제 경로 찾기
        log.info("file.getReal_filename() {}", file.getReal_filename());
        File deleteFile = new File(file.getReal_filename());

        if (deleteFile.exists()) {
            // 파일이 존재한다면 삭제
            if (deleteFile.delete()) {
                log.info("파일 삭제 성공");
            } else {
                log.info("파일 삭제 실패");
            }
        }
    }

    public String getBoardToken() {
        // 토큰 생성
        final String token = UUID.randomUUID().toString();
        boardTokenMapper.insert(token);
        return token;
    }

    @Transactional
    public Long uploadBoardImage(String token, MultipartFile file) {
        // 실제로 파일을 물리적으로 저장, DB에 저장한다.
        // writeFile 메서드랑 아래부분만 다르네..흠...
        if (file == null) return null;

        Calendar now = Calendar.getInstance();
        // 저장위치를 오늘의 날짜를 사용해 만든다. format은 위에 설정해놓았다.
        // 년도/월/날짜 구조를 가진 디렉터리 내부에 저장한다.
        String realFolder = DATA_FORMAT.format(now.getTime());

        // 실제 저장 위치를 생성한다.
        // ex)  "c:\\upload-mini\2024\4\15"가 된다.
        File realPath = new File(CURR_IMAGE_REPO_PATH + realFolder);

        //오늘 날짜에 대한 폴더가 없으면 생성한다.
        if (!realPath.exists()) {
            realPath.mkdirs();
        }

        // 실제 파일명으로 사용할 이름을 생성한다
        String fileNameReal = UUID.randomUUID().toString(); // 고유한 파일명을 위해 UUID를 사용
        // 파일 실제 경로와 파일 이름을 사용해 File 객체를 생성한다.
        File realFile = new File(realPath, fileNameReal); // (파일경로, 파일명)

        // 파일을 실제 위치에 저장한다
        try {
            // 여기서 file이 실제 파일 정보가 들어있는 객체이다.
            // realFile은 파일의 위치이다.
            file.transferTo(realFile);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("transferTo : {}", e);
            return null;
        }

        // 저장된 첨부파일 객체를 리턴한다
        // 게시물에 내용에 추가되는 이미지 파일 객체를 생성한다
        BoardImageFileVO boardImageFileVO = BoardImageFileVO.builder()
                .token(token) // 임시 토큰을 사용해 객체를 생성
                .content_type(file.getContentType())
                .original_filename(file.getOriginalFilename())
                .real_filename(realFile.getAbsolutePath())
                .size(file.getSize())
                .build();

        // DB에 이미지 저장
        // insert가 완료된 후에는 boardImageFileVO에 해당 이미지의 id 값을 넣어준다.
        // - Mybatis의 selectKey
        boardImageFileMapper.insert(boardImageFileVO);

        // 생성된 이미지의 id 값을 반환
        return boardImageFileVO.getId();
    }

    public BoardImageFileVO getBoardImageFile(String id) {
        return boardImageFileMapper.getBoardImageFile(id);
    }
}
