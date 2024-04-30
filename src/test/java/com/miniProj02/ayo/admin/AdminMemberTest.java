package com.miniProj02.ayo.admin;

import com.miniProj02.ayo.entity.MemberVO;
import com.miniProj02.ayo.member.MemberMapper;
import com.miniProj02.ayo.passwordEncoder.CustomPasswordEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdminMemberTest {
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private CustomPasswordEncoder customPasswordEncoder;

    @Test
    public void createTestSampleData() {
        for (int i = 0; i < 200; i++) {
            MemberVO memberVO = MemberVO.builder()
                    .id("member" + i).password(customPasswordEncoder.encode("1004")).address("새 멤버 주소" + i).phone("010-0000-0000").birthdate("2020-02-22").gender("F").name("회원" + i)
                    .build();
            memberMapper.insert(memberVO);
        }
    }
}
