//package com.app.hungrify.main.exception;
//
//import com.app.hungrify.main.dto.resturant.ErrorResponse;
//import com.app.hungrify.main.exception.BadRequestException;
//import com.app.hungrify.main.exception.NotFoundException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.time.Instant;
//import java.util.HashMap;
//import java.util.Map;
//
//@RestControllerAdvice
//@Slf4j
//public class RestExceptionHandler {
//
//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
//        ErrorResponse r = ErrorResponse.builder().code("NOT_FOUND").message(ex.getMessage()).timestamp(Instant.now()).build();
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(r);
//    }
//
//    @ExceptionHandler(BadRequestException.class)
//    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
//        ErrorResponse r = ErrorResponse.builder().code("BAD_REQUEST").message(ex.getMessage()).timestamp(Instant.now()).build();
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(r);
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
//        String msg = ex.getBindingResult().getFieldErrors().stream().findFirst().map(e -> e.getField() + ": " + e.getDefaultMessage()).orElse("Validation failed");
//        ErrorResponse r = ErrorResponse.builder().code("VALIDATION_ERROR").message(msg).timestamp(Instant.now()).build();
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(r);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
//        ErrorResponse r = ErrorResponse.builder().code("INTERNAL_ERROR").message("An unexpected error occurred").timestamp(Instant.now()).build();
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(r);
//    }
//}
