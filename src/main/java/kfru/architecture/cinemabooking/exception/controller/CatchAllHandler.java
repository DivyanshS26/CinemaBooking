package kfru.architecture.cinemabooking.exception.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CatchAllHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> catchAll(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(500).body(ex.getMessage());
    }
}
