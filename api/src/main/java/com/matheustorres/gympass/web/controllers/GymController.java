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
import com.matheustorres.gympass.domain.usecases.FetchNearbyGymsUseCase;
import com.matheustorres.gympass.domain.usecases.SearchGymsUseCase;
import com.matheustorres.gympass.web.dtos.request.GymSearchRequestDTO;
import com.matheustorres.gympass.web.dtos.request.NearbyGymsRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/gyms")
@RequiredArgsConstructor
public class GymController {

    private final CreateGymUseCase createGymUseCase;
    private final FetchNearbyGymsUseCase fetchNearbyGymsUseCase;
    private final SearchGymsUseCase searchGymsUseCase;

    @PostMapping
    public ResponseEntity<GymResponseDTO> create(@Valid @RequestBody GymRequestDTO request) {
        Gym gym = createGymUseCase.execute(request);
        return ResponseEntity.status(201).body(GymResponseDTO.from(gym));
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<GymResponseDTO>> nearby(@Valid @ModelAttribute NearbyGymsRequestDTO request) {
        List<Gym> gyms = fetchNearbyGymsUseCase.execute(request);
        return ResponseEntity.ok(gyms.stream()
                .map(GymResponseDTO::from)
                .collect(Collectors.toList()));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<GymResponseDTO>> search(
            @RequestParam("q") String query,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<Gym> gyms = searchGymsUseCase.execute(query, pageable);
        return ResponseEntity.ok(gyms.map(GymResponseDTO::from));
    }
}
