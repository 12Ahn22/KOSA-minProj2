package com.miniProj02.ayo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardImageFileVO { // 첨부 파일 DB 테이블과 매핑되는 VO
    private Long id;
    private Long board_id;
    private String token;
    private String original_filename;
    private String real_filename;
    private String content_type;
    private long size;
    private String created_at;

    //업로드 파일
    private MultipartFile upload;
}