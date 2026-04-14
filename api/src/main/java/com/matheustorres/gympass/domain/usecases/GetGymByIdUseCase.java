package com.matheustorres.gympass.domain.usecases;

import com.matheustorres.gympass.domain.exceptions.ResourceNotFoundException;
import com.matheustorres.gympass.domain.models.Gym;
import com.matheustorres.gympass.domain.repositories.GymRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetGymByIdUseCase {

    private final GymRepository gymRepository;

    @Transactional(readOnly = true)
    public Gym execute(String id) {
        return gymRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Academia não encontrada com o ID: " + id));
    }
}
