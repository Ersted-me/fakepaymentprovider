package net.ersted.fakepaymentprovider.exception.controller;

import net.ersted.fakepaymentprovider.dto.SimpleExceptionDto;
import net.ersted.fakepaymentprovider.exception.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthExceptionControllerAdvice {
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<SimpleExceptionDto> handleAuthException(AuthException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new SimpleExceptionDto(exception.getStatus(), exception.getMessage()));
    }
}