package com.matheustorres.gympass.web.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GymRequestDTO(
        @Schema(description = "Título da academia", example = "JS Academy")
        @NotBlank(message = "O título é obrigatório") String title,

        @Schema(description = "Descrição da academia", example = "Melhor academia da região")
        String description,

        @Schema(description = "Telefone de contato", example = "11999999999")
        String phone,

        @Schema(description = "Latitude da localização", example = "-27.2092052")
        @NotNull(message = "Latitude é obrigatória") @DecimalMin(value = "-90.0", message = "Latitude mínima é -90") @DecimalMax(value = "90.0", message = "Latitude máxima é 90") Double latitude,

        @Schema(description = "Longitude da localização", example = "-49.6401091")
        @NotNull(message = "Longitude é obrigatória") @DecimalMin(value = "-180.0", message = "Longitude mínima é -180") @DecimalMax(value = "180.0", message = "Longitude máxima é 180") Double longitude) {

}
