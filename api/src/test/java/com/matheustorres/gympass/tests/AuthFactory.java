package com.matheustorres.gympass.tests;

import com.matheustorres.gympass.web.dtos.request.LoginRequestDTO;
import com.matheustorres.gympass.web.dtos.request.RegisterUserRequestDTO;

public class AuthFactory {

    public static RegisterUserRequestDTO createRegisterRequestDTO() {
        return new RegisterUserRequestDTO("John Doe", "john@example.com", "password123");
    }

    public static LoginRequestDTO createLoginRequestDTO() {
        return new LoginRequestDTO("john@example.com", "password123");
    }
}
