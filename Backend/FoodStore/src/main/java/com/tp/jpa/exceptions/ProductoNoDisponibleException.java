package com.tp.jpa.exceptions;

public class ProductoNoDisponibleException extends RuntimeException {
    public ProductoNoDisponibleException(String message) {
        super(message);
    }
}
