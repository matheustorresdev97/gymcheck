package com.matheustorres.gympass.domain.usecases;

import com.matheustorres.gympass.domain.models.Gym;
import com.matheustorres.gympass.domain.repositories.GymRepository;
import com.matheustorres.gympass.web.dtos.request.GymRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateGymUseCase {

    private final GymRepository gymRepository;

    @Transactional
    public Gym execute(GymRequestDTO dto) {
        Gym gym = Gym.builder()
                .id(UUID.randomUUID().toString())
                .title(dto.title())
                .description(dto.description())
                .phone(dto.phone())
                .latitude(BigDecimal.valueOf(dto.latitude()))
                .longitude(BigDecimal.valueOf(dto.longitude()))
                .build();

        return gymRepository.save(gym);
    }
}
