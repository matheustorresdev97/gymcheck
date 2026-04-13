package com.matheustorres.gympass.web.dtos.response;

import lombok.Builder;

@Builder
public record LoginResponseDTO(
        UserResponseDTO user,
        String token
) {
}
