package com.matheustorres.gympass.domain.exceptions;

public class LateCheckInValidationException extends RuntimeException {
    public LateCheckInValidationException() {
        super("O check-in só pode ser validado até 20 minutos após sua criação.");
    }
}
