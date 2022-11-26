package ru.alov.market.auth.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.alov.market.api.exception.AppError;
import ru.alov.market.api.exception.ResourceNotFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionsHandler {
    @ExceptionHandler
    public ResponseEntity<AppError> handleResourceNotFoundException(ResourceNotFoundException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppError("RESOURCE_NOT_FOUND", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleFieldValidationException(FieldValidationException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppError(("VALIDATION_ERROR"), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleIllegalStateException(IllegalStateException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppError("ILLEGAL_DATA_STATE", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> handleBadCredentialsException(BadCredentialsException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppError("BAD_CREDENTIALS", e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
