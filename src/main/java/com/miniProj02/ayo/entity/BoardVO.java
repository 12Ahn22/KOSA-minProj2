package com.miniProj02.ayo.entity;

import lombok.Data;

@Data
public class BoardVO {
    private long id;
    private String title;
    private String author;
    private String content;
    private String password;
    private int view_count;
    private String created_at;
}
