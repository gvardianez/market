package ru.alov.market.cart.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import ru.alov.market.api.exception.*;

@ControllerAdvice
@Slf4j
public class GlobalExceptionsHandler {

    @ExceptionHandler
    public ResponseEntity<CartServiceAppError> handleFieldValidationException(FieldValidationException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new CartServiceAppError(CartServiceAppError.CartServiceErrors.CART_SERVICE_FIELD_VALIDATION.name(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<CartServiceAppError> catchResourceNotFoundException(ResourceNotFoundException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new CartServiceAppError(CartServiceAppError.CartServiceErrors.CART_SERVICE_RESOURCE_NOT_FOUND.name(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<CartServiceAppError> catchWebClientRequestException(WebClientRequestException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new CartServiceAppError(CartServiceAppError.CartServiceErrors.CART_SERVICE_WEBCLIENT_REQUEST.name(), e.getMessage()), HttpStatus.REQUEST_TIMEOUT);
    }

    @ExceptionHandler
    public ResponseEntity<CartServiceAppError> catchCoreServiceIntegrationException(CoreServiceIntegrationException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new CartServiceAppError(CartServiceAppError.CartServiceErrors.CART_SERVICE_CORE_INTEGRATION.name(), e.getMessage()), HttpStatus.FAILED_DEPENDENCY);
    }

    @ExceptionHandler
    public ResponseEntity<CartServiceAppError> catchAnotherException(Exception e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new CartServiceAppError(CartServiceAppError.CartServiceErrors.CART_SERVICE_INTERNAL_EXCEPTION.name(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
