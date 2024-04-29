package com.miniProj02.ayo.member;

import com.miniProj02.ayo.entity.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService implements UserDetailsService {
    private final MemberMapper memberMapper;

    public int insert(MemberVO memberVO) {
        return memberMapper.insert(memberVO);
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

    // 디비에 저장할 암호화된 값을 만들기 위한 콘솔에 찍기위한 코드이다 (무시가능)
    public static void main(String[] args) {
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bcryptPasswordEncoder.encode("1004"));
        System.out.println(bcryptPasswordEncoder.encode("0123456789010234567890123456789"));
    }

}
