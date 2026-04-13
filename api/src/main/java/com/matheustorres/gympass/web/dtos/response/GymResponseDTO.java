package com.matheustorres.gympass.web.dtos.response;

import com.matheustorres.gympass.domain.models.Gym;
import lombok.Builder;

@Builder
public record GymResponseDTO(
        String id,
        String title,
        String description,
        String phone,
        Double latitude,
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
