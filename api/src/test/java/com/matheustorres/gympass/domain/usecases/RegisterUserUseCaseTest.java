package com.matheustorres.gympass.domain.usecases;

import com.matheustorres.gympass.domain.exceptions.RoleNotFoundException;
import com.matheustorres.gympass.domain.exceptions.UserAlreadyExistsException;
import com.matheustorres.gympass.domain.models.Role;
import com.matheustorres.gympass.domain.models.User;
import com.matheustorres.gympass.domain.models.enums.UserRole;
import com.matheustorres.gympass.domain.repositories.RoleRepository;
import com.matheustorres.gympass.domain.repositories.UserRepository;
import com.matheustorres.gympass.tests.AuthFactory;
import com.matheustorres.gympass.tests.UserFactory;
import com.matheustorres.gympass.web.dtos.request.RegisterUserRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @InjectMocks
    private RegisterUserUseCase registerUserUseCase;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private RegisterUserRequestDTO requestDTO;
    private Role memberRole;

    @BeforeEach
    void setUp() {
        requestDTO = AuthFactory.createRegisterRequestDTO();
        memberRole = UserFactory.createRole();
    }

    @Test
    @DisplayName("Deve registrar um usuário com sucesso")
    void shouldRegisterUserSuccessfully() {
        // Arrange
        when(userRepository.findByEmail(requestDTO.email())).thenReturn(Optional.empty());
        when(roleRepository.findByAuthority(UserRole.MEMBER.getRole())).thenReturn(Optional.of(memberRole));
        when(passwordEncoder.encode(requestDTO.password())).thenReturn("encrypted_password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        String resultId = registerUserUseCase.register(requestDTO);

        // Assert
        assertNotNull(resultId);
        verify(userRepository, times(1)).findByEmail(requestDTO.email());
        verify(roleRepository, times(1)).findByAuthority(UserRole.MEMBER.getRole());
        verify(passwordEncoder, times(1)).encode(requestDTO.password());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar UserAlreadyExistsException quando o e-mail já estiver em uso")
    void shouldThrowExceptionWhenEmailExists() {
        // Arrange
        when(userRepository.findByEmail(requestDTO.email())).thenReturn(Optional.of(UserFactory.createUser()));

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> registerUserUseCase.register(requestDTO));
        verify(userRepository, times(1)).findByEmail(requestDTO.email());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar RoleNotFoundException quando a role MEMBER não existir")
    void shouldThrowExceptionWhenRoleNotFound() {
        // Arrange
        when(userRepository.findByEmail(requestDTO.email())).thenReturn(Optional.empty());
        when(roleRepository.findByAuthority(UserRole.MEMBER.getRole())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RoleNotFoundException.class, () -> registerUserUseCase.register(requestDTO));
        verify(roleRepository, times(1)).findByAuthority(UserRole.MEMBER.getRole());
        verify(userRepository, never()).save(any(User.class));
    }
}
