package com.matheustorres.gympass.web.dtos.response;

import lombok.Builder;

@Builder
public record UserMetricsResponseDTO(
        long checkInsCount
) {
}
