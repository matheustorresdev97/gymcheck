package com.matheustorres.gympass.domain.usecases;

import com.matheustorres.gympass.domain.exceptions.RoleNotFoundException;
import com.matheustorres.gympass.domain.exceptions.UserAlreadyExistsException;
import com.matheustorres.gympass.domain.models.Role;
import com.matheustorres.gympass.domain.models.User;
import com.matheustorres.gympass.domain.models.enums.UserRole;
import com.matheustorres.gympass.domain.repositories.RoleRepository;
import com.matheustorres.gympass.domain.repositories.UserRepository;
import com.matheustorres.gympass.web.dtos.request.RegisterUserRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String register(RegisterUserRequestDTO dto) {
        userRepository.findByEmail(dto.email())
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException();
                });

        Role memberRole = roleRepository.findByAuthority(UserRole.MEMBER.getRole())
                .orElseThrow(() -> new RoleNotFoundException(UserRole.MEMBER.getRole()));

        String passwordHash = passwordEncoder.encode(dto.password());

        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .name(dto.name())
                .email(dto.email())
                .passwordHash(passwordHash)
                .roles(Set.of(memberRole))
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
        return user.getId();
    }
}
