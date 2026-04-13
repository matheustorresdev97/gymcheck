package com.matheustorres.gympass.web.controllers;

import com.matheustorres.gympass.domain.models.enums.UserRole;
import com.matheustorres.gympass.web.dtos.request.LoginRequestDTO;
import com.matheustorres.gympass.web.dtos.request.RegisterUserRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerIntegrationTest extends AbstractIT {

    @Test
    @DisplayName("Deve registrar um novo usuário com sucesso")
    void shouldRegisterNewUser() throws Exception {
        RegisterUserRequestDTO registerDTO = new RegisterUserRequestDTO("John Doe", "john@example.com", "password123");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Não deve registrar usuário com e-mail já existente")
    void shouldNotRegisterDuplicatedUser() throws Exception {
        createAndRegisterUser("Existente", "duplicate@example.com", "password123", UserRole.MEMBER);

        RegisterUserRequestDTO registerDTO = new RegisterUserRequestDTO("Novo", "duplicate@example.com", "password123");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Deve fazer login com sucesso e retornar token")
    void shouldLoginSuccessfully() throws Exception {
        createAndRegisterUser("John", "john@example.com", "password123", UserRole.MEMBER);

        LoginRequestDTO loginDTO = new LoginRequestDTO("john@example.com", "password123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("Não deve fazer login com credenciais inválidas")
    void shouldNotLoginWithInvalidCredentials() throws Exception {
        createAndRegisterUser("John", "john@example.com", "password123", UserRole.MEMBER);

        LoginRequestDTO loginDTO = new LoginRequestDTO("john@example.com", "wrongpassword");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized());
    }
}
