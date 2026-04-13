package com.matheustorres.gympass.domain.usecases;

import com.matheustorres.gympass.domain.exceptions.ResourceNotFoundException;
import com.matheustorres.gympass.domain.models.CheckIn;
import com.matheustorres.gympass.domain.models.Gym;
import com.matheustorres.gympass.domain.models.User;
import com.matheustorres.gympass.domain.repositories.CheckInRepository;
import com.matheustorres.gympass.domain.repositories.GymRepository;
import com.matheustorres.gympass.domain.repositories.UserRepository;
import com.matheustorres.gympass.web.dtos.request.CheckInRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateCheckInUseCase {

    private final CheckInRepository checkInRepository;
    private final UserRepository userRepository;
    private final GymRepository gymRepository;

    @Transactional
    public CheckIn execute(CheckInRequestDTO dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Gym gym = gymRepository.findById(dto.gymId())
                .orElseThrow(() -> new ResourceNotFoundException("Gym not found"));

        CheckIn checkIn = new CheckIn();
        checkIn.setId(UUID.randomUUID().toString());
        checkIn.setUser(user);
        checkIn.setGym(gym);

        return checkInRepository.save(checkIn);
    }
}
