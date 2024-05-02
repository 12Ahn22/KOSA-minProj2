package com.miniProj02.ayo.board;

import com.miniProj02.ayo.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardMapper boardMapper;

    // 첨부 파일을 저장할 경로
    private final String CURR_IMAGE_REPO_PATH = "c:\\upload-mini2";
    private final BoardFileMapper boardFileMapper;

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

    public BoardFileVO getBoardFile(Long id) {
        return boardFileMapper.getFile(BoardVO.builder().id(id).build());
    }

    public BoardVO checkPassword(BoardVO boardVO) {
        return boardMapper.checkPassword(boardVO);
    }

    public int delete(BoardVO boardVO) {
        return boardMapper.delete(boardVO);
    }

    public int update(BoardVO boardVO) {
        return boardMapper.update(boardVO);
    }

    public int insert(BoardVO boardVO) {
        int updated = boardMapper.insert(boardVO);
        log.info("insert(boardVO) = {}", boardVO);

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
        return updated;
    }

    /**
     * MultipartFile 객체를 실제로 물리적인 공간에 저장하는 메서드
     */
    private BoardFileVO writeFile(MultipartFile file) {
        if (file == null || file.getName() == null) return null;

        Calendar now = Calendar.getInstance();
        // 저장위치를 오늘의 날짜를 사용해 만든다. format은 위에 설정해놓았다.
        // 년도/월/날짜 구조를 가진 디렉터리 내부에 저장한다.
        String realFolder = DATA_FORMAT.format(now.getTime());

        // 실제 저장 위치를 생성한다.
        // ex)  "c:\\upload-mini\2024\4\15"가 된다.
        File realPath = new File(CURR_IMAGE_REPO_PATH + realFolder);

        //오늘 날짜에 대한 폴더가 없으면 생성한다.
        if(!realPath.exists()) {
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
}
