package com.matheustorres.gympass.web.controllers;

import com.matheustorres.gympass.domain.usecases.RegisterUserUseCase;
import com.matheustorres.gympass.web.dtos.request.RegisterUserRequestDTO;
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

    private final RegisterUserUseCase useCase;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterUserRequestDTO dto) {
        useCase.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
