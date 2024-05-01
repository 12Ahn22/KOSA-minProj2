package com.miniProj02.ayo.admin;

import com.miniProj02.ayo.code.CodeService;
import com.miniProj02.ayo.entity.CodeVO;
import com.miniProj02.ayo.entity.MemberVO;
import com.miniProj02.ayo.entity.PageRequestVO;
import com.miniProj02.ayo.entity.PageResponseVO;
import com.miniProj02.ayo.member.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    private final MemberService memberService;
    private final CodeService codeService;

    @GetMapping("list")
    public String list(@Valid PageRequestVO pageRequestVO, BindingResult bindingResult, Model model){
        log.info("=admin/list=");
        PageResponseVO<MemberVO> pageResponseVO = memberService.list(pageRequestVO);
        List<CodeVO> codeList = codeService.getList();

        model.addAttribute("pageResponseVO", pageResponseVO);
        model.addAttribute("sizes", codeList);
        return "admin/list";
    }

    @GetMapping("view")
    public String view(@RequestParam String id, Model model){
        log.info("=admin/view id:{}=", id);
        MemberVO memberVO = memberService.view(MemberVO.builder().id(id).build());
        model.addAttribute("member", memberVO);
        return "admin/view";
    }

    @PostMapping("delete")
    @ResponseBody
    public Map<String, Object> delete(@RequestBody MemberVO memberVO, HttpSession session){
        log.info("=Admin/delete Member=");
        log.info("=MemberVO = {}", memberVO);
        Map<String, Object> map = new HashMap<>();
        int updated = memberService.delete(memberVO);
        
        if(updated == 1) { // 성공
            map.put("status", 204);
        } else {
            map.put("status", 404);
            map.put("statusMessage", "회원 삭제에 실패했습니다.");
        }
        return map;
    }
}
