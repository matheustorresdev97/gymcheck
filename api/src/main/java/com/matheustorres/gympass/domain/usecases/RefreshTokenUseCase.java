package com.matheustorres.gympass.domain.usecases;

import com.matheustorres.gympass.domain.models.User;
import com.matheustorres.gympass.domain.repositories.UserRepository;
import com.matheustorres.gympass.infra.security.TokenService;
import com.matheustorres.gympass.web.controllers.exceptions.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenUseCase {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    public String execute(String refreshToken) {
        String email = tokenService.validateToken(refreshToken);

        if (email.isEmpty()) {
            throw new ForbiddenException("Token de atualização inválido ou expirado");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ForbiddenException("Usuário não encontrado"));

        return tokenService.generateAccessToken(user);
    }
}
