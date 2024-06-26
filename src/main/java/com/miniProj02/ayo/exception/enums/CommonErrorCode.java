package com.miniProj02.ayo.exception.enums;

import com.miniProj02.ayo.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum CommonErrorCode implements ErrorCode {
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid parameter included"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not exists"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    NOT_CHANGED(HttpStatus.INTERNAL_SERVER_ERROR,"Internal server error - NOT CHANGED"),
    DUPLICATED(HttpStatus.BAD_REQUEST, "Already in use"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
