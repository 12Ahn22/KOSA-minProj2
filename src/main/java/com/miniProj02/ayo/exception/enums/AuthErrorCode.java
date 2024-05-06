package com.miniProj02.ayo.exception.enums;

import com.miniProj02.ayo.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum AuthErrorCode implements ErrorCode {
    INACTIVE_USER(HttpStatus.FORBIDDEN, "Authentication is required."),

    RESOURCE_ACCESS_ERROR(HttpStatus.BAD_REQUEST, "Not the owner of the resource.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
