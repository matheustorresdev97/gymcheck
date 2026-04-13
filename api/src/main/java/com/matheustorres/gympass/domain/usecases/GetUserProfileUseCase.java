package com.matheustorres.gympass.domain.usecases;

import com.matheustorres.gympass.domain.models.User;
import com.matheustorres.gympass.domain.repositories.UserRepository;
import com.matheustorres.gympass.domain.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserProfileUseCase {

    private final UserRepository userRepository;

    public User execute(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + userId));
    }
}