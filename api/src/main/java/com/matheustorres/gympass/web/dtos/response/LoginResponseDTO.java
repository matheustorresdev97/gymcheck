package com.matheustorres.gympass.web.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record LoginResponseDTO(
        @Schema(description = "Dados do usuário autenticado")
        UserResponseDTO user,

        @Schema(description = "Token de acesso JWT")
        String token
) {
}
