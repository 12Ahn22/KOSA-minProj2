package com.miniProj02.ayo.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/member")
@Slf4j
public class MemberController {

    @GetMapping("login")
    public String login(){
        log.info("=Login=");
        return "member/loginForm";
    }

    @GetMapping("insert")
    public String insert(){
        log.info("=Member Insert Form=");
        return "member/insertForm";
    }

    @PostMapping("insert")
    @ResponseBody
    public Map<String, Object> insert(@RequestBody MemberVO memberVO){
        log.info("=Insert Member= {}", memberVO);
        return null;
    }
}
