package com.matheustorres.gympass.web.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @Schema(description = "E-mail do usuário", example = "john@example.com")
        @NotBlank(message = "E-mail é obrigatório") @Email(message = "E-mail inválido") String email,

        @Schema(description = "Senha do usuário", example = "123456")
        @NotBlank(message = "Senha é obrigatória") String password
) {
}
