package com.miniProj02.ayo.board;

import com.miniProj02.ayo.entity.BoardFileVO;
import com.miniProj02.ayo.entity.BoardImageFileVO;
import com.miniProj02.ayo.entity.BoardVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BoardImageFileMapper {
    void insert(BoardImageFileVO boardImageFileVO);

    BoardImageFileVO getBoardImageFile(String id);
    List<BoardImageFileVO> getBoardImages(String token);

    void deleteBoardImageFiles(Map<String, Object> map);

    void updateImageBoardId(BoardVO boardVO);
}
