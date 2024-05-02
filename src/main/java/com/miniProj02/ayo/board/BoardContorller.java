package com.miniProj02.ayo.board;

import com.miniProj02.ayo.code.CodeService;
import com.miniProj02.ayo.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("list")
    public String list(PageRequestVO pageRequestVO, BindingResult bindingResult, Model model){
        log.info("=board/list=");
        PageResponseVO<BoardVO> pageResponseVO = boardService.getList(pageRequestVO);
        List<CodeVO> codeList = codeService.getList();
        log.info("pageResponseVO {}", pageResponseVO);

        if(bindingResult.hasErrors()){
            log.info("error {}", bindingResult.getFieldErrors());
            return "error";
        }

        model.addAttribute("pageResponseVO", pageResponseVO);
        model.addAttribute("sizes", codeList);
        return "board/list";
    }

    @GetMapping("insert")
    public String insertForm(){
        return "board/insertForm";
    }

    @PostMapping("insert")
    @ResponseBody
    public Map<String, Object> insert(@RequestBody BoardVO boardVO){
        Map<String, Object> map = new HashMap<>();

        return map;
    }

    @GetMapping("update")
    public String updateForm(){
        return "board/updateForm";
    }

    @PostMapping("update")
    public Map<String, Object> update(){
        Map<String, Object> map = new HashMap<>();

        return map;
    }

    @GetMapping("view")
    @ResponseBody
    public Map<String, Object> view(BoardVO boardVO){
        Map<String, Object> map = new HashMap<>();
        BoardVO findBoardVO = boardService.view(boardVO);
        if(findBoardVO != null){
            map.put("board", findBoardVO);
            map.put("status", 204);
        }else{
            map.put("status", 404);
        }
        return map;
    }

    @PostMapping("delete")
    @ResponseBody
    public Map<String, Object> delete(@RequestBody BoardVO boardVO){
        Map<String, Object> map = new HashMap<>();
        return map;
    }
}
