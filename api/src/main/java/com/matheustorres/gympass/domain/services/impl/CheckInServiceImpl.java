package com.matheustorres.gympass.domain.services.impl;

import com.matheustorres.gympass.domain.repositories.CheckInRepository;
import com.matheustorres.gympass.domain.services.CheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckInServiceImpl implements CheckInService {

    private final CheckInRepository checkInRepository;

}
