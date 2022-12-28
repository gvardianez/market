package ru.alov.market.gateway.exceptions;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.alov.market.api.exception.AppError;

@ControllerAdvice
@Slf4j
public class GlobalExceptionsHandler {
    @ExceptionHandler
    public ResponseEntity<AppError> handleJwtException(JwtException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppError("TOKEN_EXCEPTION", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @ExceptionHandler
//    public ResponseEntity<AppError> handleIllegalStateException(IllegalStateException e) {
//        log.error(e.getMessage(), e);
//        return new ResponseEntity<>(new AppError("ILLEGAL_DATA_STATE", e.getMessage()), HttpStatus.BAD_REQUEST);
//    }

}
