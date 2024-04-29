package com.miniProj02.ayo.member;

import com.miniProj02.ayo.entity.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/member")
@Slf4j
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

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
        Map<String, Object> map = new HashMap<>();

        int updated = memberService.insert(memberVO);
        // 이 아래를 한번에 처리 하는 방법이 없으려나?
        if(updated == 1) { // 성공
            map.put("status", 204);
        } else {
            map.put("status", 404);
            map.put("statusMessage", "게시글 삭제에 실패하였습니다");
        }
        return map;
    }
}
