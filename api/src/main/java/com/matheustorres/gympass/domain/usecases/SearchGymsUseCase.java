package com.matheustorres.gympass.domain.usecases;

import com.matheustorres.gympass.domain.models.Gym;
import com.matheustorres.gympass.domain.repositories.GymRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchGymsUseCase {

    private final GymRepository gymRepository;

    @Transactional(readOnly = true)
    public Page<Gym> execute(String query, Pageable pageable) {
        return gymRepository.findByTitleContainingIgnoreCase(query, pageable);
    }
}
