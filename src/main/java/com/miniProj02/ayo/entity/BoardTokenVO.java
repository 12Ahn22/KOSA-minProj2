package com.miniProj02.ayo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardTokenVO {
    private String token;
    private int status;
    private String create_at;
}