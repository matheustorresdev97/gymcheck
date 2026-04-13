package com.matheustorres.gympass.domain.services.impl;

import com.matheustorres.gympass.domain.repositories.GymRepository;
import com.matheustorres.gympass.domain.services.GymService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GymServiceImpl implements GymService {

    private final GymRepository gymRepository;

}
