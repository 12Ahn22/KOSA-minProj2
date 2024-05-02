package com.miniProj02.ayo.board;

import com.miniProj02.ayo.entity.BoardVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoardTest {
    @Autowired
    private BoardMapper boardMapper;

    @Test
    public void createTestSampleData() {
        for (int i = 0; i < 200; i++) {
            BoardVO boardVO = BoardVO.builder()
                    .title("새로 만든 게시글" + i)
                    .content("게시글 내용입니다." + i)
                    .author("admin")
                    .password("1234")
                    .build();
            boardMapper.insert(boardVO);
        }
    }
}
