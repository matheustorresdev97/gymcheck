package com.matheustorres.gympass.web.controllers;

import com.matheustorres.gympass.domain.usecases.AuthenticateUserUseCase;
import com.matheustorres.gympass.domain.usecases.LoginResult;
import com.matheustorres.gympass.domain.usecases.RefreshTokenUseCase;
import com.matheustorres.gympass.domain.usecases.RegisterUserUseCase;
import com.matheustorres.gympass.web.dtos.request.LoginRequestDTO;
import com.matheustorres.gympass.web.dtos.request.RegisterUserRequestDTO;
import com.matheustorres.gympass.web.dtos.response.LoginResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Autenticação", description = "Endpoints para registro, login e renovação de tokens")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    @Operation(summary = "Registrar um novo usuário", description = "Cria um novo usuário no sistema com a role MEMBER por padrão.")
    @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso")
    @ApiResponse(responseCode = "409", description = "E-mail já cadastrado")
    @PostMapping("/auth/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterUserRequestDTO dto) {
        registerUserUseCase.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Autenticar usuário", description = "Realiza o login e retorna um token de acesso, além de setar um refresh token nos cookies.")
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
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

    @Operation(summary = "Renovar token de acesso", description = "Gera um novo token de acesso JWT a partir de um refresh token válido nos cookies.")
    @ApiResponse(responseCode = "200", description = "Token renovado com sucesso")
    @ApiResponse(responseCode = "401", description = "Refresh token inválido ou ausente")
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
