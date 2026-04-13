package com.matheustorres.gympass.web.dtos.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GymRequestDTO(
        @NotBlank(message = "O título é obrigatório") String title,

        String description,

        String phone,

        @NotNull(message = "Latitude é obrigatória") @DecimalMin(value = "-90.0", message = "Latitude mínima é -90") @DecimalMax(value = "90.0", message = "Latitude máxima é 90") Double latitude,

        @NotNull(message = "Longitude é obrigatória") @DecimalMin(value = "-180.0", message = "Longitude mínima é -180") @DecimalMax(value = "180.0", message = "Longitude máxima é 180") Double longitude) {

}
