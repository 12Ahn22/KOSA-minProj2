package com.miniProj02.ayo.member;

import com.miniProj02.ayo.entity.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        if(resultVO == null) {
            log.info("사용자가 존재하지 않습니다.");
            throw new UsernameNotFoundException(username + " 사용자가 존재하지 않습니다");
        }
        log.info("resultVO = {}", resultVO);
        return resultVO;
    }


}
