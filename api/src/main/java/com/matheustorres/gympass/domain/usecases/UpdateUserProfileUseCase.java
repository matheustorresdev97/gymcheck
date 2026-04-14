package com.matheustorres.gympass.domain.usecases;

import com.matheustorres.gympass.domain.exceptions.ResourceNotFoundException;
import com.matheustorres.gympass.domain.exceptions.UserAlreadyExistsException;
import com.matheustorres.gympass.domain.models.User;
import com.matheustorres.gympass.domain.repositories.UserRepository;
import com.matheustorres.gympass.web.dtos.request.UserUpdateRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateUserProfileUseCase {

    private final UserRepository userRepository;

    @Transactional
    public User execute(String id, UserUpdateRequestDTO data) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        if (data.email() != null && !data.email().equalsIgnoreCase(user.getEmail())) {
            userRepository.findByEmail(data.email()).ifPresent(u -> {
                throw new UserAlreadyExistsException("E-mail já está em uso por outro usuário.");
            });
            user.setEmail(data.email());
        }

        if (data.name() != null) {
            user.setName(data.name());
        }

        return userRepository.save(user);
    }
}
