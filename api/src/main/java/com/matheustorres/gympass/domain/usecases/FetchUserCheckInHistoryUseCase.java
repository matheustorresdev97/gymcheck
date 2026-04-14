package com.matheustorres.gympass.domain.usecases;

import com.matheustorres.gympass.domain.models.CheckIn;
import com.matheustorres.gympass.domain.models.User;
import com.matheustorres.gympass.domain.models.enums.UserRole;
import com.matheustorres.gympass.domain.repositories.CheckInRepository;
import com.matheustorres.gympass.web.controllers.exceptions.ForbiddenException;
import com.matheustorres.gympass.web.dtos.response.CheckInResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FetchUserCheckInHistoryUseCase {

    private final CheckInRepository checkInRepository;

    @Transactional(readOnly = true)
    public Page<CheckInResponseDTO> execute(User requester, String targetUserId, Pageable pageable) {
        boolean isAdmin = requester.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(UserRole.ADMIN.getRole()));

        Page<CheckIn> history;

        if (targetUserId != null && !targetUserId.isBlank()) {
            if (!isAdmin && !requester.getId().equals(targetUserId)) {
                throw new ForbiddenException("Você não tem permissão para ver o histórico deste usuário.");
            }
            history = checkInRepository.findByUserId(targetUserId, pageable);
        } else if (isAdmin) {
            history = checkInRepository.findAll(pageable);
        } else {
            history = checkInRepository.findByUserId(requester.getId(), pageable);
        }

        // Mapping inside Transactional avoids LazyInitializationException
        return history.map(CheckInResponseDTO::from);
    }
}
