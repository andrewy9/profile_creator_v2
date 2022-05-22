package com.andrew.profile_creator.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(RoleTypeNotFoundException.class)
    protected ResponseEntity<Object> handleRoleTypeNotFoundException( RoleTypeNotFoundException e) {
        ApiError apiError = new ApiError(NOT_FOUND);
        apiError.setMessage(e.getMessage());
        return ResponseEntity.badRequest().body(apiError);
    }


}
