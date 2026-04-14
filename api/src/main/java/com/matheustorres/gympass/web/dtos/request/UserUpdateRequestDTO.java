package com.matheustorres.gympass.web.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequestDTO(
        @Schema(description = "Novo nome do usuário", example = "Matheus Torres")
        @Size(min = 3, message = "O nome deve ter pelo menos 3 caracteres")
        String name,

        @Schema(description = "Novo e-mail do usuário", example = "novo@email.com")
        @Email(message = "E-mail inválido")
        String email
) {
}
