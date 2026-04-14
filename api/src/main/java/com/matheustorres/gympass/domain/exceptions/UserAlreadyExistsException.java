package com.matheustorres.gympass.domain.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("E-mail já cadastrado");
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
