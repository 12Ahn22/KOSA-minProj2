package com.miniProj02.ayo.board;

import com.miniProj02.ayo.entity.BoardVO;
import com.miniProj02.ayo.entity.MemberVO;
import com.miniProj02.ayo.entity.PageRequestVO;
import com.miniProj02.ayo.entity.PageResponseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardMapper boardMapper;

    public PageResponseVO<BoardVO> getList(PageRequestVO pageRequestVO) {
        List<BoardVO> list = null;
        PageResponseVO<BoardVO> pageResponseVO = null;
        int total = 0;

        list = boardMapper.getList(pageRequestVO);
        total = boardMapper.getTotalCount(pageRequestVO);

        pageResponseVO = new PageResponseVO<>(list, total, pageRequestVO.getPageNo(), pageRequestVO.getSize());
        return pageResponseVO;
    }

    public BoardVO view(BoardVO boardVO, Authentication authentication) {
        // 로그인한 세션 계정
        MemberVO loginMember = (MemberVO) authentication.getPrincipal();
        if (!boardVO.getAuthor().equals(loginMember.getId())) {
            // 동일하지않다면 view_count 증가
            boardMapper.increaseViewCount(boardVO);
        }
        BoardVO findBoardVO = boardMapper.view(boardVO);
        return findBoardVO;
    }

    public BoardVO checkPassword(BoardVO boardVO) {
        return boardMapper.checkPassword(boardVO);
    }

    public int delete(BoardVO boardVO) {
        return boardMapper.delete(boardVO);
    }

    public int update(BoardVO boardVO) {
        return boardMapper.update(boardVO);
    }

    public int insert(BoardVO boardVO) {
        return boardMapper.insert(boardVO);
    }
}
