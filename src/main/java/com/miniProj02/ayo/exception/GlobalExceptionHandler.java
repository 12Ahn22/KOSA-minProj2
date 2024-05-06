package com.miniProj02.ayo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
// 발생한 예외를 처리하는 글로벌 예외 핸들러
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(MyException.class)
    public ResponseEntity<Object> handleCustomException(MyException e){
        ErrorCode errorCode = e.getErrorCode();
        log.warn("Custom Error, Message = {}, HttpStatus = {}", errorCode.getMessage(), errorCode.getHttpStatus());
      return handleExceptionInternal(errorCode);
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .status(errorCode.getHttpStatus().value())
                .build();
    }
}
