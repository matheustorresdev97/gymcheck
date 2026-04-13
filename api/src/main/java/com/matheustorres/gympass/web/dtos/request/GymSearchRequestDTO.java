package com.matheustorres.gympass.web.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record GymSearchRequestDTO(
        @NotBlank(message = "Query term is required") String q
) {
}
