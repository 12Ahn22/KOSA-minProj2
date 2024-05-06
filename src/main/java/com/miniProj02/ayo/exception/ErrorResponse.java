package com.miniProj02.ayo.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse {
    // 오류 응답 형식 클래스
    private final String code;
    private final String message;
    private final Integer status;
}
