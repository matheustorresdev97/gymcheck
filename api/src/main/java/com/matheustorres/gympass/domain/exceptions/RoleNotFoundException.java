package com.matheustorres.gympass.domain.exceptions;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String role) {
        super("Role '" + role + "' não encontrada");
    }
}
