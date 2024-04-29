package com.miniProj02.ayo.member;

import com.miniProj02.ayo.entity.MemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberMapper memberMapper;

    public int insert(MemberVO memberVO) {
        return memberMapper.insert(memberVO);
    }
}
