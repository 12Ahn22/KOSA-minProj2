package com.miniProj02.ayo.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardVO {
    private Long id;
    private String title;
    private String author;
    private String content;
    private String password;
    private Integer view_count;
    private String created_at;
}
