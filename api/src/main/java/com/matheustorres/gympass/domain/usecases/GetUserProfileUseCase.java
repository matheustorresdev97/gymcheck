package com.matheustorres.gympass.domain.usecases;

import com.matheustorres.gympass.domain.models.User;
import com.matheustorres.gympass.domain.repositories.UserRepository;
import com.matheustorres.gympass.domain.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetUserProfileUseCase {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User execute(String userId) {
        return userRepository.findByIdWithRoles(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + userId));
    }
}