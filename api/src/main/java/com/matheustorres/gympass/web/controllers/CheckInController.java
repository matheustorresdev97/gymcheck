package com.matheustorres.gympass.web.controllers;

import com.matheustorres.gympass.domain.models.CheckIn;
import com.matheustorres.gympass.domain.usecases.CreateCheckInUseCase;
import com.matheustorres.gympass.web.dtos.request.CheckInRequestDTO;
import com.matheustorres.gympass.web.dtos.response.CheckInResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/check-ins")
@RequiredArgsConstructor
public class CheckInController {

    private final CreateCheckInUseCase createCheckInUseCase;

    @PostMapping
    public ResponseEntity<CheckInResponseDTO> create(@RequestBody @Valid CheckInRequestDTO dto) {
        CheckIn checkIn = createCheckInUseCase.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(CheckInResponseDTO.from(checkIn));
    }
}
