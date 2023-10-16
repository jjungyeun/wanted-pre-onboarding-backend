package com.wjy.wantedpreonboarding.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<Map<String, String>> handleCustomException(CustomException e){
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus())
                .body(Map.of("message", errorCode.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Map<String, String>> handleException(Exception e){
        log.error(e.getMessage(), e);
        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(Map.of("message", e.getMessage()));
    }
}
