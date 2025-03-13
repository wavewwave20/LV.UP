package com.alltogether.lvupbackend.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ExceptionMsgDto> handleAppException(AppException ex) {
        return ResponseEntity.status(ex.getStatus()).body(new ExceptionMsgDto(ex.getMessage() + "\n 에러 상세:\n"+ ex.toString()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionMsgDto> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionMsgDto("서버 에러가 발생했습니다." + "\n 에러 상세:\n"+ ex.toString()));
    }
}

