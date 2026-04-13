package com.matheustorres.gympass.web.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record UserMetricsResponseDTO(
        @Schema(description = "Total de check-ins realizados pelo usuário", example = "42")
        long checkInsCount
) {
}
