package com.matheustorres.gympass.web.controllers;

import com.matheustorres.gympass.domain.models.CheckIn;
import com.matheustorres.gympass.domain.models.User;
import com.matheustorres.gympass.domain.usecases.CreateCheckInUseCase;
import com.matheustorres.gympass.domain.usecases.FetchUserCheckInHistoryUseCase;
import com.matheustorres.gympass.domain.usecases.GetUserMetricsUseCase;
import com.matheustorres.gympass.domain.usecases.ValidateCheckInUseCase;
import com.matheustorres.gympass.web.dtos.request.CheckInCreateRequestDTO;
import com.matheustorres.gympass.web.dtos.response.CheckInResponseDTO;
import com.matheustorres.gympass.web.dtos.response.UserMetricsResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Check-ins", description = "Endpoints para realização, validação e histórico de check-ins")
@SecurityRequirement(name = "bearerAuth")
public class CheckInController {

    private final CreateCheckInUseCase createCheckInUseCase;
    private final FetchUserCheckInHistoryUseCase fetchUserCheckInHistoryUseCase;
    private final GetUserMetricsUseCase getUserMetricsUseCase;
    private final ValidateCheckInUseCase validateCheckInUseCase;

    @Operation(summary = "Realizar check-in em uma academia", description = "O usuário deve estar a no máximo 100 metros da academia e não pode ter feito outro check-in no mesmo dia.")
    @ApiResponse(responseCode = "201", description = "Check-in realizado com sucesso")
    @ApiResponse(responseCode = "403", description = "Distância máxima excedida ou limite diário atingido")
    @PostMapping("/{gymId}")
    public ResponseEntity<CheckInResponseDTO> create(
            @PathVariable String gymId,
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CheckInCreateRequestDTO dto
    ) {
        CheckIn checkIn = createCheckInUseCase.execute(user.getId(), gymId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(CheckInResponseDTO.from(checkIn));
    }

    @Operation(summary = "Validar um check-in", description = "Somente usuários com a role ADMIN podem validar check-ins. A validação só é possível até 20 minutos após a criação.")
    @ApiResponse(responseCode = "200", description = "Check-in validado com sucesso")
    @ApiResponse(responseCode = "400", description = "Tempo de validação expirado")
    @ApiResponse(responseCode = "403", description = "Acesso negado - requer role ADMIN")
    @PatchMapping("/validate/{checkInId}")
    public ResponseEntity<CheckInResponseDTO> validate(@PathVariable String checkInId) {
        CheckIn checkIn = validateCheckInUseCase.execute(checkInId);
        return ResponseEntity.ok(CheckInResponseDTO.from(checkIn));
    }

    @Operation(summary = "Obter histórico de check-ins", description = "Retorna o histórico de check-ins do usuário autenticado. Admins podem passar o userId para ver o histórico de outros.")
    @ApiResponse(responseCode = "200", description = "Histórico retornado com sucesso")
    @GetMapping("/history")
    public ResponseEntity<Page<CheckInResponseDTO>> history(
            @AuthenticationPrincipal User requester,
            @RequestParam(required = false) String userId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<CheckInResponseDTO> history = fetchUserCheckInHistoryUseCase.execute(requester, userId, pageable);
        return ResponseEntity.ok(history);
    }

    @Operation(summary = "Obter métricas de check-ins", description = "Retorna a contagem total de check-ins realizados por um usuário.")
    @ApiResponse(responseCode = "200", description = "Métricas retornadas com sucesso")
    @GetMapping("/metrics")
    public ResponseEntity<UserMetricsResponseDTO> metrics(@RequestParam String userId) {
        long count = getUserMetricsUseCase.execute(userId);
        return ResponseEntity.ok(new UserMetricsResponseDTO(count));
    }
}
