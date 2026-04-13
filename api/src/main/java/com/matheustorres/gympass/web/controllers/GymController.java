package com.matheustorres.gympass.web.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matheustorres.gympass.domain.models.Gym;
import com.matheustorres.gympass.web.dtos.request.GymRequestDTO;
import com.matheustorres.gympass.web.dtos.response.GymResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.matheustorres.gympass.domain.usecases.CreateGymUseCase;

@RestController
@RequestMapping("/gyms")
@RequiredArgsConstructor
public class GymController {

    private final CreateGymUseCase createGymUseCase;

    @PostMapping
    public ResponseEntity<GymResponseDTO> create(@Valid @RequestBody GymRequestDTO request) {
        Gym gym = createGymUseCase.execute(request);
        return ResponseEntity.status(201).body(GymResponseDTO.from(gym));
    }
}
