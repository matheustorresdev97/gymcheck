package com.matheustorres.gympass.domain.exceptions;

public class MaxDistanceException extends RuntimeException {
    public MaxDistanceException() {
        super("Sua distância até a academia é superior ao limite permitido.");
    }
}
