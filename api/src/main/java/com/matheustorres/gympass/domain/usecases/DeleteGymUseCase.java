package com.matheustorres.gympass.domain.usecases;

import com.matheustorres.gympass.domain.exceptions.ResourceNotFoundException;
import com.matheustorres.gympass.domain.repositories.GymRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteGymUseCase {

    private final GymRepository gymRepository;

    @Transactional
    public void execute(String id) {
        if (!gymRepository.existsById(id)) {
            throw new ResourceNotFoundException("Academia não encontrada com o ID: " + id);
        }
        gymRepository.deleteById(id);
    }
}
