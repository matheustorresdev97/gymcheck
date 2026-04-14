package com.matheustorres.gympass.web.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.matheustorres.gympass.domain.models.CheckIn;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CheckInResponseDTO(
        @Schema(description = "ID único do check-in", example = "550e8400-e29b-41d4-a716-446655440000")
        String id,

        @Schema(description = "ID do usuário", example = "550e8400-e29b-41d4-a716-446655440001")
        String userId,

        @Schema(description = "ID da academia", example = "550e8400-e29b-41d4-a716-446655440002")
        String gymId,

        @Schema(description = "Nome da academia", example = "JS Academy")
        String gymTitle,

        @Schema(description = "Data de criação")
        LocalDateTime createdAt,

        @Schema(description = "Data de validação (apenas se validado por um ADMIN)")
        LocalDateTime validatedAt) {

    public static CheckInResponseDTO from(CheckIn checkIn) {
        return CheckInResponseDTO.builder()
                .id(checkIn.getId())
                .userId(checkIn.getUser().getId())
                .gymId(checkIn.getGym().getId())
                .gymTitle(checkIn.getGym().getTitle())
                .createdAt(checkIn.getCreatedAt())
                .validatedAt(checkIn.getValidatedAt())
                .build();
    }
}
