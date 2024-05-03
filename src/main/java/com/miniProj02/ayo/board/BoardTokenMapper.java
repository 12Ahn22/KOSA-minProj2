package com.miniProj02.ayo.board;

import com.miniProj02.ayo.entity.BoardFileVO;
import com.miniProj02.ayo.entity.BoardTokenVO;
import com.miniProj02.ayo.entity.BoardVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BoardTokenMapper {
    void insert(String token);

    void updateState(String token);

    List<BoardTokenVO> listToken();

    void deleteTokens(Map<String, Object> map);
}
