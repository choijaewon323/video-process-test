package com.swmaestro.shorts.controller;

import com.swmaestro.shorts.exception.InvalidVideoFormatException;
import com.swmaestro.shorts.exception.VideoProcessFailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(VideoProcessFailException.class)
    public ResponseEntity<?> videoProcessFailExceptionHandler(final VideoProcessFailException e) {
        final ErrorResponse errorResponse = ErrorResponse.builder(e, HttpStatus.INTERNAL_SERVER_ERROR, "동영상 처리 실패")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    @ExceptionHandler(InvalidVideoFormatException.class)
    public ResponseEntity<?> invalidVideoFormatExceptionHandler(final InvalidVideoFormatException e) {
        final ErrorResponse errorResponse = ErrorResponse.builder(e, HttpStatus.INTERNAL_SERVER_ERROR, "잘못된 비디오 형식")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}
