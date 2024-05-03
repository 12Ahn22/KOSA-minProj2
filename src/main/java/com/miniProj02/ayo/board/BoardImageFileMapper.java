package com.miniProj02.ayo.board;

import com.miniProj02.ayo.entity.BoardFileVO;
import com.miniProj02.ayo.entity.BoardImageFileVO;
import com.miniProj02.ayo.entity.BoardVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardImageFileMapper {
    void insert(BoardImageFileVO boardImageFileVO);

    BoardImageFileVO getBoardImageFile(String id);
}