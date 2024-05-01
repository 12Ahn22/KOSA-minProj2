package com.miniProj02.ayo.board;

import com.miniProj02.ayo.entity.BoardVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/board")
public class BoardContorller {
    @GetMapping("list")
    public String list(){
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
    public String view(){
        return "board/view";
    }

    @PostMapping("delete")
    @ResponseBody
    public Map<String, Object> delete(@RequestBody BoardVO boardVO){
        Map<String, Object> map = new HashMap<>();
        return map;
    }
}
