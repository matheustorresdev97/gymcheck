package com.matheustorres.gympass.domain.usecases;

import com.matheustorres.gympass.domain.exceptions.LateCheckInValidationException;
import com.matheustorres.gympass.domain.exceptions.ResourceNotFoundException;
import com.matheustorres.gympass.domain.models.CheckIn;
import com.matheustorres.gympass.domain.repositories.CheckInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ValidateCheckInUseCase {

    private final CheckInRepository checkInRepository;

    @Transactional
    public CheckIn execute(String checkInId) {
        CheckIn checkIn = checkInRepository.findById(checkInId)
                .orElseThrow(() -> new ResourceNotFoundException("Check-in não encontrado"));

        long distanceInMinutes = Duration.between(checkIn.getCreatedAt(), LocalDateTime.now()).toMinutes();

        if (distanceInMinutes > 20) {
            throw new LateCheckInValidationException();
        }

        checkIn.setValidatedAt(LocalDateTime.now());

        return checkInRepository.save(checkIn);
    }
}
