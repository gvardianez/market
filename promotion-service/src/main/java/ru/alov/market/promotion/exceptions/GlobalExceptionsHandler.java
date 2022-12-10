package ru.alov.market.promotion.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.alov.market.api.exception.*;

@ControllerAdvice
@Slf4j
public class GlobalExceptionsHandler {

    @ExceptionHandler
    public ResponseEntity<PromotionServiceAppError> handleResourceNotFoundException(ResourceNotFoundException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new PromotionServiceAppError(PromotionServiceAppError.PromotionServiceErrors.PROMOTION_SERVICE_RESOURCE_NOT_FOUND.name(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<PromotionServiceAppError> catchCoreServiceIntegrationException(CoreServiceIntegrationException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new PromotionServiceAppError(PromotionServiceAppError.PromotionServiceErrors.PROMOTION_SERVICE_CORE_INTEGRATION.name(), e.getMessage()), HttpStatus.FAILED_DEPENDENCY);
    }

    @ExceptionHandler
    public ResponseEntity<PromotionServiceAppError> catchAnotherException(Exception e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new PromotionServiceAppError(PromotionServiceAppError.PromotionServiceErrors.PROMOTION_SERVICE_INTERNAL_EXCEPTION.name(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
