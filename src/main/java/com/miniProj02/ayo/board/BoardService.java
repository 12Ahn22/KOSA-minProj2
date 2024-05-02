package com.miniProj02.ayo.board;

import com.miniProj02.ayo.entity.BoardVO;
import com.miniProj02.ayo.entity.PageRequestVO;
import com.miniProj02.ayo.entity.PageResponseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardMapper boardMapper;
    public PageResponseVO<BoardVO> getList(PageRequestVO pageRequestVO) {
        PageResponseVO<BoardVO> pageResponseVO = null;
        int total = 0;

        List<BoardVO> list = boardMapper.getList(pageRequestVO);
        total = boardMapper.getTotalCount(pageRequestVO);

        pageResponseVO = new PageResponseVO<>(list,total, pageRequestVO.getPageNo(), pageRequestVO.getSize());
        return pageResponseVO;
    }

    public BoardVO view(BoardVO boardVO) {
        return boardMapper.view(boardVO);
    }
}
