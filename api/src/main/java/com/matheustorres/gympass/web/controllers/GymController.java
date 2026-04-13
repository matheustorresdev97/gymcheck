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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Academias", description = "Endpoints para gestão e busca de academias")
public class GymController {

    private final CreateGymUseCase createGymUseCase;
    private final FetchNearbyGymsUseCase fetchNearbyGymsUseCase;
    private final SearchGymsUseCase searchGymsUseCase;

    @Operation(summary = "Criar uma nova academia", description = "Somente usuários com a role ADMIN podem acessar este endpoint.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "201", description = "Academia criada com sucesso")
    @ApiResponse(responseCode = "403", description = "Acesso negado - requer role ADMIN")
    @PostMapping
    public ResponseEntity<GymResponseDTO> create(@Valid @RequestBody GymRequestDTO request) {
        Gym gym = createGymUseCase.execute(request);
        return ResponseEntity.status(201).body(GymResponseDTO.from(gym));
    }

    @Operation(summary = "Buscar academias próximas", description = "Retorna uma lista de academias em um raio de 10km da localização informada. Endpoint público.")
    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    @GetMapping("/nearby")
    public ResponseEntity<List<GymResponseDTO>> nearby(@Valid @ModelAttribute NearbyGymsRequestDTO request) {
        List<Gym> gyms = fetchNearbyGymsUseCase.execute(request);
        return ResponseEntity.ok(gyms.stream()
                .map(GymResponseDTO::from)
                .collect(Collectors.toList()));
    }

    @Operation(summary = "Buscar academias pelo nome", description = "Retorna uma lista paginada de academias que contém o termo pesquisado no título. Endpoint público.")
    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    @GetMapping("/search")
    public ResponseEntity<Page<GymResponseDTO>> search(
            @RequestParam("q") String query,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<Gym> gyms = searchGymsUseCase.execute(query, pageable);
        return ResponseEntity.ok(gyms.map(GymResponseDTO::from));
    }
}
