package com.tp.jpa.exceptions;

public class PrecioNoValidoException extends NumberFormatException {
    public PrecioNoValidoException(String mensaje) {
        super(mensaje);
    }
}
