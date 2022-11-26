package ru.alov.market.cart.exceptions;

public class FieldValidationException extends RuntimeException{
    public FieldValidationException(String message) {
        super(message);
    }
}
