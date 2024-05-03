package com.miniProj02.ayo.entity;

import com.miniProj02.ayo.board.FileVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardImageFileVO implements FileVO {
    private Long id;
    private Long board_id;
    private String original_filename;
    private String real_filename;
    private String content_type;
    private long size;
    private String created_at;
    private String token; // 토큰
    private MultipartFile upload; // 업로드 파일
}