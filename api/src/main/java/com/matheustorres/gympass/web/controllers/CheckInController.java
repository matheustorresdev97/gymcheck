package com.matheustorres.gympass.web.controllers;

import com.matheustorres.gympass.domain.models.CheckIn;
import com.matheustorres.gympass.domain.models.User;
import com.matheustorres.gympass.domain.usecases.CreateCheckInUseCase;
import com.matheustorres.gympass.domain.usecases.FetchUserCheckInHistoryUseCase;
import com.matheustorres.gympass.web.dtos.request.CheckInRequestDTO;
import com.matheustorres.gympass.web.dtos.response.CheckInResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/check-ins")
@RequiredArgsConstructor
public class CheckInController {

    private final CreateCheckInUseCase createCheckInUseCase;
    private final FetchUserCheckInHistoryUseCase fetchUserCheckInHistoryUseCase;

    @PostMapping
    public ResponseEntity<CheckInResponseDTO> create(@RequestBody @Valid CheckInRequestDTO dto) {
        CheckIn checkIn = createCheckInUseCase.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(CheckInResponseDTO.from(checkIn));
    }

    @GetMapping("/history")
    public ResponseEntity<Page<CheckInResponseDTO>> history(
            @AuthenticationPrincipal User requester,
            @RequestParam(required = false) String userId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<CheckIn> history = fetchUserCheckInHistoryUseCase.execute(requester, userId, pageable);
        return ResponseEntity.ok(history.map(CheckInResponseDTO::from));
    }
}
