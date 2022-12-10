package ru.alov.market.auth.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.alov.market.api.exception.*;

@ControllerAdvice
@Slf4j
public class GlobalExceptionsHandler {
    @ExceptionHandler
    public ResponseEntity<AuthServiceAppError> handleResourceNotFoundException(ResourceNotFoundException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AuthServiceAppError(AuthServiceAppError.AuthServiceErrors.AUTH_SERVICE_RESOURCE_NOT_FOUND.name(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AuthServiceAppError> handleFieldValidationException(FieldValidationException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AuthServiceAppError(AuthServiceAppError.AuthServiceErrors.AUTH_SERVICE_FIELD_VALIDATION.name(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AuthServiceAppError> handleIllegalStateException(IllegalStateException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AuthServiceAppError(AuthServiceAppError.AuthServiceErrors.AUTH_SERVICE_BAD_REQUEST.name(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AuthServiceAppError> handleBadCredentialsException(BadCredentialsException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AuthServiceAppError(AuthServiceAppError.AuthServiceErrors.AUTH_SERVICE_BAD_CREDENTIALS.name(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AuthServiceAppError> handleSecurityException(SecurityException e){
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AuthServiceAppError(AuthServiceAppError.AuthServiceErrors.AUTH_SERVICE_SECURITY.name(), e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<AuthServiceAppError> catchAnotherException(Exception e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AuthServiceAppError(AuthServiceAppError.AuthServiceErrors.AUTH_SERVICE_INTERNAL_EXCEPTION.name(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
