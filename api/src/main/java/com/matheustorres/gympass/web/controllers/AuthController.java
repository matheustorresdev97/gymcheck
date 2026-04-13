package com.matheustorres.gympass.web.controllers;

import com.matheustorres.gympass.domain.usecases.AuthenticateUserUseCase;
import com.matheustorres.gympass.domain.usecases.RegisterUserUseCase;
import com.matheustorres.gympass.web.dtos.request.LoginRequestDTO;
import com.matheustorres.gympass.web.dtos.request.RegisterUserRequestDTO;
import com.matheustorres.gympass.web.dtos.response.LoginResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterUserRequestDTO dto) {
        registerUserUseCase.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        LoginResponseDTO response = authenticateUserUseCase.execute(dto);
        return ResponseEntity.ok(response);
    }
}
