package com.example.Auth.Pr2.controllers.impl;

import com.example.Auth.Pr2.commons.exceptions.UserNotFoundException;
import com.example.Auth.Pr2.commons.dtos.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {
    @ExceptionHandler(value = {UserNotFoundException.class})
    ResponseEntity<ErrorResponse> handleError(UserNotFoundException userNotFoundException) {
        log.error("new Exception", userNotFoundException);
        var errorResponse = ErrorResponse.builder()
                .codeStatus(userNotFoundException.getHttpStatus().value())
                .message(userNotFoundException.getMessage())
                .build();
        return ResponseEntity.status(userNotFoundException.getHttpStatus()).body(errorResponse);
    }
}
