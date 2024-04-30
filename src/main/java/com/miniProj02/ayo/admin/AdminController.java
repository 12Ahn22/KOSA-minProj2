package com.miniProj02.ayo.admin;

import com.miniProj02.ayo.code.CodeService;
import com.miniProj02.ayo.entity.CodeVO;
import com.miniProj02.ayo.entity.MemberVO;
import com.miniProj02.ayo.entity.PageRequestVO;
import com.miniProj02.ayo.entity.PageResponseVO;
import com.miniProj02.ayo.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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
        log.info("codeList =>{}", codeList);

        model.addAttribute("pageResponseVO", pageResponseVO);
        model.addAttribute("sizes", codeList);
        return "admin/list";
    }
}
