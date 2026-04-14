package com.matheustorres.gympass.web.controllers;

import com.matheustorres.gympass.domain.models.User;
import com.matheustorres.gympass.domain.usecases.GetUserProfileUseCase;
import com.matheustorres.gympass.domain.usecases.UpdateUserProfileUseCase;
import com.matheustorres.gympass.domain.usecases.FetchAllUsersUseCase;
import com.matheustorres.gympass.web.dtos.request.UserUpdateRequestDTO;
import com.matheustorres.gympass.web.dtos.response.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Endpoints para gestão de perfil de usuário")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final GetUserProfileUseCase getUserProfileUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final FetchAllUsersUseCase fetchAllUsersUseCase;

    @Operation(summary = "Obter perfil do usuário logado", description = "Retorna os dados detalhados do usuário autenticado pelo token JWT.")
    @ApiResponse(responseCode = "200", description = "Perfil retornado com sucesso")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMe(@AuthenticationPrincipal User user) {
        User profile = getUserProfileUseCase.execute(user.getId());
        return ResponseEntity.ok(UserResponseDTO.from(profile));
    }

    @Operation(summary = "Atualizar perfil do usuário logado", description = "Permite que o usuário altere seu nome ou e-mail.")
    @ApiResponse(responseCode = "200", description = "Perfil atualizado com sucesso")
    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateMe(@AuthenticationPrincipal User user, @Valid @RequestBody UserUpdateRequestDTO request) {
        User updated = updateUserProfileUseCase.execute(user.getId(), request);
        return ResponseEntity.ok(UserResponseDTO.from(updated));
    }

    @Operation(summary = "Listar todos os usuários", description = "Somente acessível por admins.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getAll(@PageableDefault(size = 20) Pageable pageable) {
        Page<User> users = fetchAllUsersUseCase.execute(pageable);
        return ResponseEntity.ok(users.map(UserResponseDTO::from));
    }
}
