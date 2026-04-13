package com.matheustorres.gympass.web.dtos.response;

import com.matheustorres.gympass.domain.models.CheckIn;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CheckInResponseDTO(
        String id,
        String userId,
        String gymId,
        LocalDateTime createdAt) {

    public static CheckInResponseDTO from(CheckIn checkIn) {
        return CheckInResponseDTO.builder()
                .id(checkIn.getId())
                .userId(checkIn.getUser().getId())
                .gymId(checkIn.getGym().getId())
                .createdAt(checkIn.getCreatedAt())
                .build();
    }
}
