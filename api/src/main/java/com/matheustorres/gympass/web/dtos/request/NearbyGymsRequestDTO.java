package com.matheustorres.gympass.web.dtos.request;

import jakarta.validation.constraints.NotNull;

public record NearbyGymsRequestDTO(
        @NotNull(message = "Latitude is required") Double latitude,
        @NotNull(message = "Longitude is required") Double longitude
) {
}
