package com.miniProj02.ayo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardVO {
    private Long id;
    private String title;
    private String author;
    private String content;
    private String password;
    private Integer view_count;
    private String created_at;
    private Boolean isNew;

    public Boolean isNew() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime boardTime = LocalDateTime.parse(created_at, formatter); // 게시물이 생성된 시간
        LocalDateTime pivotTime = LocalDateTime.now().minusHours(24); // 지금부터 24시간 전

        return pivotTime.isBefore(boardTime);
    }
}
