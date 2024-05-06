package com.miniProj02.ayo.entity;

import com.miniProj02.ayo.board.FileVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardFileVO implements FileVO { // 첨부 파일 DB 테이블과 매핑되는 VO
    private Long id;
    private Long board_id;
    private String original_filename;
    private String real_filename;
    private String content_type;
    private Long size;
    private String created_at;
}