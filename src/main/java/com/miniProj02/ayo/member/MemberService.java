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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService implements UserDetailsService {
    private final MemberMapper memberMapper;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public int insert(MemberVO memberVO) {
        memberVO.hashPassword(bCryptPasswordEncoder);
        return memberMapper.insert(memberVO);
    }

    public MemberVO view(MemberVO memberVO) {
        return memberMapper.view(memberVO);
    }


    @Transactional
    public int delete(MemberVO memberVO) {
        return memberMapper.delete(memberVO);
    }

    @Transactional
    public int update(MemberVO memberVO) {
        if(!memberVO.getPassword().equals("") && memberVO.getPassword() != null) memberVO.hashPassword(bCryptPasswordEncoder);
        return memberMapper.update(memberVO);
    }

    @Transactional
    public int adminUpdate(MemberVO memberVO) {
        if(!memberVO.getPassword().equals("") && memberVO.getPassword() != null) memberVO.hashPassword(bCryptPasswordEncoder);
        // 계정 잠금 값이 = null이면, "N"값
        if(memberVO.getAccount_locked() == null) memberVO.setAccount_locked("N");
        // 권한 검사 (일단 보류)
        return memberMapper.adminUpdate(memberVO);
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
//            return null; // 사용자가 존재하지 않는 경우 null 반환
        }

        // 로그인 시도 카운트 증가
        memberMapper.loginCountInc(resultVO);
        return resultVO;
    }


    public PageResponseVO<MemberVO> list(PageRequestVO pageRequestVO) {
        List<MemberVO> list = null;
        PageResponseVO<MemberVO> pageResponseVO = null;
        int total = 0;
        list = memberMapper.getList(pageRequestVO);
        total = memberMapper.getTotalCount(pageRequestVO);

        pageResponseVO =
                new PageResponseVO<MemberVO>(list, total, pageRequestVO.getPageNo(), pageRequestVO.getSize());

        return pageResponseVO;
    }

    @Transactional
    public int updateAccountLock(MemberVO memberVO) {
        return memberMapper.updateAccountLock(memberVO);
    }
}
