package com.miniProj02.ayo.member;

import com.miniProj02.ayo.entity.MemberFormDTO;
import com.miniProj02.ayo.entity.MemberVO;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/member")
@Slf4j
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("loginForm")
    public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String exception, Model model) {
        log.info("=loginForm=");
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "member/loginForm";
    }

    @GetMapping("insert")
    public String insert() {
        log.info("=Member Insert Form=");
        return "member/insertForm";
    }

    @PostMapping("insert")
    @ResponseBody
    public Map<String, Object> insert(@RequestBody @Valid MemberFormDTO memberFormDTO, BindingResult bindingResult) {
        log.info("=Insert Member= {}", memberFormDTO);
        Map<String, Object> map = new HashMap<>();

        MemberVO memberVO = MemberVO.builder()
                .id(memberFormDTO.getId())
                .name(memberFormDTO.getName())
                .password(memberFormDTO.getPassword())
                .birthdate(memberFormDTO.getBirthdate())
                .phone(memberFormDTO.getPhone())
                .address(memberFormDTO.getAddress())
                .gender(memberFormDTO.getGender())
                .build();

        if (checkMemberFormDTO(bindingResult, map, memberVO)) return map;

        int updated = memberService.insert(memberVO);
        // 이 아래를 한번에 처리 하는 방법이 없으려나?
        if (updated == 1) { // 성공
            map.put("status", 204);
        } else {
            map.put("status", 404);
            map.put("statusMessage", "회원 가입에 실패했습니다.");
        }
        return map;
    }

    @PostMapping("duplicate")
    @ResponseBody
    public Map<String, Object> isDuplicated(@RequestBody MemberVO memberVO) {
        Map<String, Object> map = new HashMap<>();
        MemberVO searchMember = memberService.view(memberVO);
        if (searchMember == null) {
            map.put("status", 204);
        } else {
            map.put("status", 404);
            map.put("statusMessage", "해당 아이디는 이미 사용중입니다.");
        }
        return map;
    }

    @GetMapping("profile")
    public String profile(Authentication authentication, Model model) {
        log.info("=Profile=");
        MemberVO memberVO = memberService.view((MemberVO) authentication.getPrincipal());
        model.addAttribute("member", memberVO);
        return "member/profile";
    }

    @GetMapping("update")
    public String update(Authentication authentication, Model model) {
        log.info("=Update Form=");
        MemberVO memberVO = memberService.view((MemberVO) authentication.getPrincipal());
        model.addAttribute("member", memberVO);
        return "member/updateForm";
    }

    @PostMapping("update")
    @ResponseBody
    public Map<String, Object> update(@RequestBody @Valid MemberFormDTO memberFormDTO, BindingResult bindingResult) {
        log.info("=UPDATE Member=");
        log.info("=MemberVO = {}", memberFormDTO);
        Map<String, Object> map = new HashMap<>();

        MemberVO memberVO = MemberVO.builder()
                .id(memberFormDTO.getId())
                .name(memberFormDTO.getName())
                .password(memberFormDTO.getPassword())
                .birthdate(memberFormDTO.getBirthdate())
                .phone(memberFormDTO.getPhone())
                .address(memberFormDTO.getAddress())
                .gender(memberFormDTO.getGender())
                .build();

        if (checkMemberFormDTO(bindingResult, map, memberVO)) return map;

        int updated = memberService.update(memberVO);

        if (updated == 1) { // 성공
            map.put("status", 204);
        } else {
            map.put("status", 404);
            map.put("statusMessage", "회원 수정에 실패했습니다.");
        }
        return map;
    }

    @PostMapping("delete")
    @ResponseBody
    public Map<String, Object> delete(@RequestBody MemberVO memberVO, HttpSession session) {
        log.info("=DELETE Member=");
        log.info("=MemberVO = {}", memberVO);
        Map<String, Object> map = new HashMap<>();

        int updated = memberService.delete(memberVO);

        if (updated == 1) { // 성공
            map.put("status", 204);
            // 세션에서 제거
            session.invalidate();
        } else {
            map.put("status", 404);
            map.put("statusMessage", "회원 탈퇴에 실패했습니다.");
        }
        return map;
    }

    /**
     * insert, update validation check
     */
    private boolean checkMemberFormDTO(BindingResult bindingResult, Map<String, Object> map, MemberVO memberVO) {
        if (bindingResult.hasErrors()) {
            log.info("member/insert error => {}", memberVO);
            
            map.put("status", 404);
            // 에러들 확인
            for (FieldError error : bindingResult.getFieldErrors()) {
                String fieldName = error.getField();
                String errorMessage = error.getDefaultMessage();
                log.error("filedName =>{}", fieldName);
                log.error("errorMessage =>{}", errorMessage);
                // 가장 마지막 에러 넣어서 보내기
                map.put("statusMessage", errorMessage);
            }
            return true;
        }
        return false;
    }
}
