package com.matheustorres.gympass.domain.usecases;

import com.matheustorres.gympass.domain.exceptions.MaxDistanceException;
import com.matheustorres.gympass.domain.exceptions.ResourceNotFoundException;
import com.matheustorres.gympass.domain.models.CheckIn;
import com.matheustorres.gympass.domain.models.Gym;
import com.matheustorres.gympass.domain.models.User;
import com.matheustorres.gympass.domain.repositories.CheckInRepository;
import com.matheustorres.gympass.domain.repositories.GymRepository;
import com.matheustorres.gympass.domain.repositories.UserRepository;
import com.matheustorres.gympass.infra.utils.Coordinate;
import com.matheustorres.gympass.infra.utils.GeoUtils;
import com.matheustorres.gympass.web.dtos.request.CheckInCreateRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateCheckInUseCase {

    private static final double MAX_DISTANCE_IN_KILOMETERS = 0.1;

    private final CheckInRepository checkInRepository;
    private final UserRepository userRepository;
    private final GymRepository gymRepository;

    @Transactional
    public CheckIn execute(String userId, String gymId, CheckInCreateRequestDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() -> new ResourceNotFoundException("Gym not found"));

        double distance = GeoUtils.getDistanceBetweenCoordinates(
                new Coordinate(dto.latitude(), dto.longitude()),
                new Coordinate(gym.getLatitude().doubleValue(), gym.getLongitude().doubleValue())
        );

        if (distance > MAX_DISTANCE_IN_KILOMETERS) {
            throw new MaxDistanceException();
        }

        CheckIn checkIn = CheckIn.builder()
                .id(UUID.randomUUID().toString())
                .user(user)
                .gym(gym)
                .build();

        return checkInRepository.save(checkIn);
    }
}
