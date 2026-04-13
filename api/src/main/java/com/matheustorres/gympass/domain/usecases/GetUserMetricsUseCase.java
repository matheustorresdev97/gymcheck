package com.matheustorres.gympass.domain.usecases;

import com.matheustorres.gympass.domain.repositories.CheckInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetUserMetricsUseCase {

    private final CheckInRepository checkInRepository;

    @Transactional(readOnly = true)
    public long execute(String userId) {
        return checkInRepository.countByUserId(userId);
    }
}
