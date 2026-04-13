package com.matheustorres.gympass.web.controllers;

import com.matheustorres.gympass.domain.models.User;
import com.matheustorres.gympass.domain.usecases.GetUserProfileUseCase;
import com.matheustorres.gympass.web.dtos.response.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final GetUserProfileUseCase getUserProfileUseCase;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMe(@AuthenticationPrincipal User user) {
        User profile = getUserProfileUseCase.execute(user.getId());
        return ResponseEntity.ok(UserResponseDTO.from(profile));
    }
}
