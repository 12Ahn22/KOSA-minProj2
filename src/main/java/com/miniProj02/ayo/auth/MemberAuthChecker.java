package com.miniProj02.ayo.auth;

import com.miniProj02.ayo.entity.MemberVO;

public class MemberAuthChecker {

    /**
     * 관리자라면 전부 가능, 일반 멤버라면 본인인 경우 
     */
    static public boolean check(String id, MemberVO memberVO){
        if(memberVO.getAuthName().equals("ADMIN")) return true;
        return id.equals(memberVO.getId());
    }
}
