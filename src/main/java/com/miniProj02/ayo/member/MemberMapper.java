package com.miniProj02.ayo.member;

import com.miniProj02.ayo.entity.MemberVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    int insert(MemberVO memberVO);
}
