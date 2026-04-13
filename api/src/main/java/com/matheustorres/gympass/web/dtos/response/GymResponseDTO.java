package com.matheustorres.gympass.web.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.matheustorres.gympass.domain.models.Gym;
import lombok.Builder;

@Builder
public record GymResponseDTO(
        @Schema(description = "ID único da academia", example = "550e8400-e29b-41d4-a716-446655440000")
        String id,

        @Schema(description = "Título", example = "JS Academy")
        String title,

        @Schema(description = "Descrição", example = "Melhor academia")
        String description,

        @Schema(description = "Telefone", example = "1199999999")
        String phone,

        @Schema(description = "Latitude", example = "-27.2092052")
        Double latitude,

        @Schema(description = "Longitude", example = "-49.6401091")
        Double longitude) {

    public static GymResponseDTO from(Gym gym) {
        return GymResponseDTO.builder()
                .id(gym.getId())
                .title(gym.getTitle())
                .description(gym.getDescription())
                .phone(gym.getPhone())
                .latitude(gym.getLatitude().doubleValue())
                .longitude(gym.getLongitude().doubleValue())
                .build();
    }
}
