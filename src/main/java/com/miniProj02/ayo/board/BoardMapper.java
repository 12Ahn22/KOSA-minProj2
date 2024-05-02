package com.miniProj02.ayo.board;

import com.miniProj02.ayo.entity.BoardVO;
import com.miniProj02.ayo.entity.MemberVO;
import com.miniProj02.ayo.entity.PageRequestVO;
import com.miniProj02.ayo.entity.PageResponseVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    List<BoardVO> getList(PageRequestVO pageRequestVO);

    int getTotalCount(PageRequestVO pageRequestVO);

    BoardVO view(BoardVO boardVO);

    BoardVO checkPassword(BoardVO boardVO);
}
