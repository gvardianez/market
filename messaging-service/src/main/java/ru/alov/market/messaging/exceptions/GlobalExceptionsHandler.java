package ru.alov.market.messaging.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.alov.market.api.exception.AnalyticsServiceAppError;
import ru.alov.market.api.exception.AppError;
import ru.alov.market.api.exception.MessagingServiceAppError;
import ru.alov.market.api.exception.ResourceNotFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionsHandler {

    @ExceptionHandler
    public ResponseEntity<MessagingServiceAppError> handleResourceNotFoundException(ResourceNotFoundException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new MessagingServiceAppError(MessagingServiceAppError.MessagingServiceErrors.MESSAGING_SERVICE_RESOURCE_NOT_FOUND.name(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<MessagingServiceAppError> handleIllegalStateException(IllegalStateException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new MessagingServiceAppError(MessagingServiceAppError.MessagingServiceErrors.MESSAGING_SERVICE_BAD_REQUEST.name(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<MessagingServiceAppError> handleSecurityException(SecurityException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new MessagingServiceAppError(MessagingServiceAppError.MessagingServiceErrors.MESSAGING_SERVICE_SECURITY.name(), e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<MessagingServiceAppError> catchAnotherException(Exception e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new MessagingServiceAppError(MessagingServiceAppError.MessagingServiceErrors.MESSAGING_SERVICE_INTERNAL_EXCEPTION.name(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
