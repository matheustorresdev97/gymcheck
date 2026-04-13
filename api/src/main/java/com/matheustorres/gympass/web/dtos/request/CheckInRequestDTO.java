package com.matheustorres.gympass.web.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CheckInRequestDTO(
        @NotBlank(message = "User ID is required") String userId,
        @NotBlank(message = "Gym ID is required") String gymId,
        @NotNull(message = "User latitude is required") Double userLatitude,
        @NotNull(message = "User longitude is required") Double userLongitude) {
}
