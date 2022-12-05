package ru.alov.market.api.exception;

public class CartServiceAppError extends AppError {

    public enum CartServiceErrors {
        CART_IS_BROKEN, CART_NOT_FOUND, CART_SERVICE_CONNECT_TIMEOUT_EXCEEDED
    }

    public CartServiceAppError(String code, String message) {
        super(code, message);
    }
}
