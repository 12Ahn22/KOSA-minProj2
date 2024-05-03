package com.miniProj02.ayo.board;

import com.miniProj02.ayo.code.CodeService;
import com.miniProj02.ayo.entity.*;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/board")
@Slf4j
@RequiredArgsConstructor
public class BoardContorller {
    private final CodeService codeService;
    private final BoardService boardService;
    private final ServletContext application;

    @GetMapping("list")
    public String list(PageRequestVO pageRequestVO, BindingResult bindingResult, Model model) {
        log.info("=board/list=");
        log.info("{}", pageRequestVO);
        PageResponseVO<BoardVO> pageResponseVO = boardService.getList(pageRequestVO);

        List<CodeVO> codeList = codeService.getList();
        log.info("pageResponseVO {}", pageResponseVO);

        if (bindingResult.hasErrors()) {
            log.info("error {}", bindingResult.getFieldErrors());
            return "error";
        }

        model.addAttribute("pageResponseVO", pageResponseVO);
        model.addAttribute("sizes", codeList);
        return "board/list";
    }

    @GetMapping("insert")
    public String insertForm(Model model) {
        // insertForm에 접근하면 게시글 이미지용 임시 토큰을 발급해준다.
        String token = boardService.getBoardToken();
        model.addAttribute("token", token);
        return "board/insertForm";
    }

    @PostMapping("insert")
    @ResponseBody
    public Map<String, Object> insert(BoardVO boardVO, Authentication authentication) {
        log.info("=board/insert=");
        log.info("boardVO = {}", boardVO);
        Map<String, Object> map = new HashMap<>();
        MemberVO loginMember = (MemberVO) authentication.getPrincipal();
        boardVO.setAuthor(loginMember.getId());

        int updated = boardService.insert(boardVO);

        if (updated == 1) {
            map.put("status", 204);
        } else {
            map.put("status", 404);
        }
        return map;
    }

    @GetMapping("update")
    public String updateForm(BoardVO boardVO, Model model) {
        BoardVO findBoardVO = boardService.fetchUpdateData(boardVO);
        String token = boardService.getBoardToken();
        model.addAttribute("token", token);
        model.addAttribute("board", findBoardVO);
        return "board/updateForm";
    }

    @PostMapping("update")
    @ResponseBody
    public Map<String, Object> update(BoardVO boardVO) {
        log.info("=board/update=");
        log.info("=boardVO = {}", boardVO);
        Map<String, Object> map = new HashMap<>();

        int updated = boardService.update(boardVO);
        if (updated == 1) {
            // 성공
            map.put("status", 204);
        } else {
            // 실패
            map.put("status", 404);
        }
        return map;
    }

    @GetMapping("view")
    @ResponseBody
    public Map<String, Object> view(BoardVO boardVO, Authentication authentication) {
        log.info("=board/view=");
        log.info("=boardVO = {}", boardVO);
        Map<String, Object> map = new HashMap<>();

        BoardVO findBoardVO = boardService.view(boardVO, authentication);

        if (findBoardVO != null) {
            map.put("board", findBoardVO);
            map.put("status", 204);
        } else {
            map.put("status", 404);
        }
        return map;
    }

    @PostMapping("checkPassword")
    @ResponseBody
    public Map<String, Object> checkPassword(@RequestBody BoardVO boardVO) {
        log.info("board/checkPassword");
        Map<String, Object> map = new HashMap<>();

        BoardVO findBoardVO = boardService.checkPassword(boardVO);

        if (findBoardVO != null) {
            map.put("board", findBoardVO);
            map.put("status", 204);
        } else {
            map.put("status", 404);
        }

        return map;
    }

    @PostMapping("delete")
    @ResponseBody
    public Map<String, Object> delete(@RequestBody BoardVO boardVO) {
        log.info("=board/delete=");
        Map<String, Object> map = new HashMap<>();

        int updated = boardService.delete(boardVO);
        if (updated == 1) {
            // 성공
            map.put("status", 204);
        } else {
            // 실패
            map.put("status", 404);
        }
        return map;
    }

    @GetMapping("fileDownload/{id}")
    public void downloadFile(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        log.info("FileDownload/{}", id);

        OutputStream out = response.getOutputStream(); // 브라우저에 출력 스트림
        BoardFileVO boardFileVO = boardService.getBoardFile(id);
        log.info("boardFileVO = {}", boardFileVO);

        if (boardFileVO == null) {
            response.setStatus(404); // 파일이 없습니다. 404
        } else {
            String originName = boardFileVO.getOriginal_filename(); // 오리지널 이름이 보여주는 이름이다.
            originName = URLEncoder.encode(originName, "UTF-8");
            //다운로드 할 때 헤더 설정 (중요)
            response.setHeader("Cache-Control", "no-cache");
            // attachment; fileName=첨부파일명 >> 반드시 ;와 띄어쓰기 주의해야한다. (HTTP 규칙임)
            response.addHeader("Content-disposition", "attachment; fileName=" + originName); // 저장 파일 명칭 설정
            response.setContentLength((int) boardFileVO.getSize()); // 전체 파일 사이즈 (예측 시간을 측정해준다.)
            response.setContentType(boardFileVO.getContent_type()); // 해당 첨부 파일의 콘텐츠 정보를 준다(파일 확장자명)

            //파일을 바이너리로 바꿔서 담아 놓고 responseOutputStream에 담아서 보낸다.
            // 바디에 관한 것이 실제 스트림 데이터이다. = 실제 파일
            // 실제 파일이기 때문에 원본 경로를 사용한다.
            FileInputStream input = new FileInputStream(new File(boardFileVO.getReal_filename()));

            //outputStream에 8k씩 전달
            byte[] buffer = new byte[1024 * 8]; // 파일을 블록단위로 읽어들이기 위해서 버퍼를 사용한다.

            while (true) {
                int count = input.read(buffer); // 파일을 읽기
                if (count < 0) break; // 파일을 다 읽었다.
                // 파일을 읽은 양을 브라우저에 기록한다.
                out.write(buffer, 0, count); // 브라우저에 기록(출력)한다.
            }
            input.close();
            out.close();
        }
    }

    @PostMapping("boardImageUpload")
    @ResponseBody
    // image는 enctype="multipart/form-data”로 요청이 온다.
    public Map<String,Object> boardImageUpload(BoardImageFileVO boardImageFileVO){
        log.info("=boardImageUpload=");
        log.info("boardImageFile => {}", boardImageFileVO);
        Map<String, Object> map = new HashMap<>();

        // ckeditor 에서 파일을 보낼 때 upload : [파일] 형식으로 해서 넘어옴, upload라는 키 이용하여 파일을 저장 한다.
        // 즉, BoardImageFileVO의 private MultipartFile upload에 실제 파일 데이터가 담겨있다.
        MultipartFile file = boardImageFileVO.getUpload(); // 업로드할 실제 파일
        String token = boardImageFileVO.getToken(); // 넘어온 임시로 사용할 토큰

        // 이미지를 받아온 임시 토큰을 사용해 DB 저장한다.
        Long uploadedFileId = boardService.uploadBoardImage(token, file);

        // 이미지를 현재 경로와 연관된 파일에 저장하기 위해 현재 경로를 알아냄
        String uploadPath = application.getContextPath() + "/board/image/" + uploadedFileId;

        // ckeditor는 이미지 업로드 후 이미지 표시하기 위해 uploaded 와 url을 json 형식으로 받아야 함
        log.info("image 경로 = {}", uploadPath);
        map.put("uploaded", true); // 업로드 완료
        map.put("url", uploadPath); // 업로드 파일의 경로

        return map;
    }

    @GetMapping("image/{id}")
    public void getBoardImage(@PathVariable String id, HttpServletResponse response) throws IOException {
        OutputStream out = response.getOutputStream();

        BoardImageFileVO boardImageFileVO = boardService.getBoardImageFile(id);

        if (boardImageFileVO == null) {
            response.setStatus(404);
        } else {
            String originName = boardImageFileVO.getOriginal_filename();
            originName = URLEncoder.encode(originName, "UTF-8");
            //다운로드 할 때 헤더 설정
            response.setHeader("Cache-Control", "no-cache");
            response.addHeader("Content-disposition", "attachment; fileName="+originName);
            response.setContentLength((int)boardImageFileVO.getSize());
            response.setContentType(boardImageFileVO.getContent_type());

            //파일을 바이너리로 바꿔서 담아 놓고 responseOutputStream에 담아서 보낸다.
            FileInputStream input = new FileInputStream(new File(boardImageFileVO.getReal_filename()));

            //outputStream에 8k씩 전달
            byte[] buffer = new byte[1024*8];

            while(true) {
                int count = input.read(buffer);
                if(count<0)break;
                out.write(buffer,0,count);
            }
            input.close();
            out.close();
        }
    }

}
