package com.myprojects.reminder.exceptionHandler;

import com.myprojects.reminder.domain.ErrorResponse;
import com.myprojects.reminder.exception.EmailNotFoundException;
import com.myprojects.reminder.exception.UserAlreadyExistException;
import com.myprojects.reminder.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.myprojects.reminder.domain.ErrorResponse.createErrorResponse;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> emailNotFound(EmailNotFoundException e, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // Readability purposes
                .body(createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), request));
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> userNotFound(UserNotFoundException e,HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), request));
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> userAlreadyExist(UserAlreadyExistException e,HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(createErrorResponse(HttpStatus.CONFLICT, e.getMessage(), request));
    }
}
