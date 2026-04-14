package com.matheustorres.gympass.domain.usecases;

import com.matheustorres.gympass.domain.exceptions.ResourceNotFoundException;
import com.matheustorres.gympass.domain.models.Gym;
import com.matheustorres.gympass.domain.repositories.GymRepository;
import com.matheustorres.gympass.web.dtos.request.GymRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UpdateGymUseCase {

    private final GymRepository gymRepository;

    @Transactional
    public Gym execute(String id, GymRequestDTO data) {
        Gym gym = gymRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Academia não encontrada com o ID: " + id));

        gym.setTitle(data.title());
        gym.setDescription(data.description());
        gym.setPhone(data.phone());
        gym.setLatitude(BigDecimal.valueOf(data.latitude()));
        gym.setLongitude(BigDecimal.valueOf(data.longitude()));

        return gymRepository.save(gym);
    }
}
