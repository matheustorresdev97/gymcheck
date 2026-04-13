package com.matheustorres.gympass.web.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record CheckInRequestDTO(
        @NotBlank(message = "User ID is required")
        String userId,
        @NotBlank(message = "Gym ID is required")
        String gymId) {
}
