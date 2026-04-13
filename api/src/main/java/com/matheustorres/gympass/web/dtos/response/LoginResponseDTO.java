package com.matheustorres.gympass.web.dtos.response;

public record LoginResponseDTO(
        UserResponseDTO user,
        String token
) {
}
