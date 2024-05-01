package com.miniProj02.ayo.member;

import com.miniProj02.ayo.entity.MemberVO;
import com.miniProj02.ayo.entity.PageRequestVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {
    int insert(MemberVO memberVO);

    MemberVO login(MemberVO build);

    MemberVO view(MemberVO memberVO);

    int delete(MemberVO memberVO);

    int update(MemberVO memberVO);

    List<MemberVO> getList(PageRequestVO pageRequestVO);

    int getTotalCount(PageRequestVO pageRequestVO);

    int adminUpdate(MemberVO memberVO);
}
