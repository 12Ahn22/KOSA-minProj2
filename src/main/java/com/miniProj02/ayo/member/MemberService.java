package com.miniProj02.ayo.member;

import com.miniProj02.ayo.entity.MemberVO;
import com.miniProj02.ayo.entity.PageRequestVO;
import com.miniProj02.ayo.entity.PageResponseVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService implements UserDetailsService {
    private final MemberMapper memberMapper;
    private final PasswordEncoder bCryptPasswordEncoder;

    public int insert(MemberVO memberVO) {
        memberVO.hashPassword(bCryptPasswordEncoder);
        return memberMapper.insert(memberVO);
    }

    public MemberVO view(MemberVO memberVO) {
        return memberMapper.view(memberVO);
    }


    public int delete(MemberVO memberVO) {
        return memberMapper.delete(memberVO);
    }

    public int update(MemberVO memberVO) {
        memberVO.hashPassword(bCryptPasswordEncoder);
        return memberMapper.update(memberVO);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("#### loadUserByUsername ####");
        MemberVO memberVO = MemberVO.builder().id(username).build();
        log.info("MemberVO = {}", memberVO);
        MemberVO resultVO = memberMapper.login(memberVO);
        if (resultVO == null) {
            log.info("사용자가 존재하지 않습니다.");
            throw new UsernameNotFoundException(username + " 사용자가 존재하지 않습니다");
        }
        log.info("resultVO = {}", resultVO);
        return resultVO;
    }


    public PageResponseVO<MemberVO> list(PageRequestVO pageRequestVO) {
        log.info("=Member List=");
        List<MemberVO> list = null;
        PageResponseVO<MemberVO> pageResponseVO = null;
        int total = 0;
        list = memberMapper.getList(pageRequestVO);
        total = memberMapper.getTotalCount(pageRequestVO);

        log.info("list {}", list);
        log.info("total {}", total);
        pageResponseVO =
                new PageResponseVO<MemberVO>(list, total, pageRequestVO.getPageNo(), pageRequestVO.getSize());

        return pageResponseVO;
    }

    public int adminUpdate(MemberVO memberVO) {
        memberVO.hashPassword(bCryptPasswordEncoder);
        // 계정 잠금 값이 = null이면, "N"값
        if(memberVO.getAccount_locked() == null) memberVO.setAccount_locked("N");
        // 권한 검사 (일단 보류)
        return memberMapper.adminUpdate(memberVO);
    }
}
