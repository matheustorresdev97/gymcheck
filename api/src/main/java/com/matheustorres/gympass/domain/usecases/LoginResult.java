package com.matheustorres.gympass.domain.usecases;

import com.matheustorres.gympass.web.dtos.response.LoginResponseDTO;

public record LoginResult(LoginResponseDTO loginResponse, String refreshToken) {
}
