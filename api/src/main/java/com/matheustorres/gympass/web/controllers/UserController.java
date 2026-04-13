package com.matheustorres.gympass.web.controllers;

import com.matheustorres.gympass.domain.models.User;
import com.matheustorres.gympass.domain.usecases.GetUserProfileUseCase;
import com.matheustorres.gympass.web.dtos.response.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Endpoints para gestão de perfil de usuário")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final GetUserProfileUseCase getUserProfileUseCase;

    @Operation(summary = "Obter perfil do usuário logado", description = "Retorna os dados detalhados do usuário autenticado pelo token JWT.")
    @ApiResponse(responseCode = "200", description = "Perfil retornado com sucesso")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMe(@AuthenticationPrincipal User user) {
        User profile = getUserProfileUseCase.execute(user.getId());
        return ResponseEntity.ok(UserResponseDTO.from(profile));
    }
}
