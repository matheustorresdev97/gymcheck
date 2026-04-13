package com.matheustorres.gympass.web.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequestDTO(
                @Schema(description = "Nome do usuário", example = "John Doe")
                @NotBlank(message = "Nome é obrigatório") String name,

                @Schema(description = "E-mail do usuário", example = "john@example.com")
                @NotBlank(message = "E-mail é obrigatório") @Email(message = "E-mail inválido") String email,

                @Schema(description = "Senha do usuário", example = "123456", minLength = 6)
                @NotBlank(message = "Senha é obrigatória") @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres") String password) {

}
