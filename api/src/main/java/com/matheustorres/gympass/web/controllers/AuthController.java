package com.matheustorres.gympass.web.controllers;

import com.matheustorres.gympass.domain.usecases.AuthenticateUserUseCase;
import com.matheustorres.gympass.domain.usecases.LoginResult;
import com.matheustorres.gympass.domain.usecases.RefreshTokenUseCase;
import com.matheustorres.gympass.domain.usecases.RegisterUserUseCase;
import com.matheustorres.gympass.web.dtos.request.LoginRequestDTO;
import com.matheustorres.gympass.web.dtos.request.RegisterUserRequestDTO;
import com.matheustorres.gympass.web.dtos.response.LoginResponseDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    @PostMapping("/auth/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterUserRequestDTO dto) {
        registerUserUseCase.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto, HttpServletResponse response) {
        LoginResult result = authenticateUserUseCase.execute(dto);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", result.refreshToken())
                .httpOnly(true)
                .secure(false) // Coloque como true em produção (HTTPS)
                .path("/")
                .maxAge(60 * 60 * 24 * 7) // 7 dias
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(result.loginResponse());
    }

    @PatchMapping("/token/refresh")
    public ResponseEntity<Map<String, String>> refresh(
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) {
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String newAccessToken = refreshTokenUseCase.execute(refreshToken);
        return ResponseEntity.ok(Map.of("token", newAccessToken));
    }
}
