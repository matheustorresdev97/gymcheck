package com.matheustorres.gympass.web.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record NearbyGymsRequestDTO(
        @Schema(description = "Latitude atual do usuário", example = "-27.2092052")
        @NotNull(message = "Latitude is required") Double latitude,

        @Schema(description = "Longitude atual do usuário", example = "-49.6401091")
        @NotNull(message = "Longitude is required") Double longitude
) {
}
