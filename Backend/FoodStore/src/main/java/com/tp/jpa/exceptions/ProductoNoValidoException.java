package com.tp.jpa.exceptions;

public class ProductoNoValidoException extends RuntimeException {
    public ProductoNoValidoException(String msj) {
        super(msj);
    }
}
