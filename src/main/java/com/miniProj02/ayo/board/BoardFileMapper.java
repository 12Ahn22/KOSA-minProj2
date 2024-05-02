package com.miniProj02.ayo.board;

import com.miniProj02.ayo.entity.BoardFileVO;
import com.miniProj02.ayo.entity.BoardVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardFileMapper {
    void insert(BoardFileVO boardFileVO);

    BoardFileVO getFile(BoardVO boardVO);
}
