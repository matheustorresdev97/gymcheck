package com.matheustorres.gympass.domain.usecases;

import com.matheustorres.gympass.domain.models.User;
import com.matheustorres.gympass.infra.security.TokenService;
import com.matheustorres.gympass.tests.AuthFactory;
import com.matheustorres.gympass.tests.UserFactory;
import com.matheustorres.gympass.web.dtos.request.LoginRequestDTO;
import com.matheustorres.gympass.web.dtos.response.LoginResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticateUserUseCaseTest {

    @InjectMocks
    private AuthenticateUserUseCase authenticateUserUseCase;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    private LoginRequestDTO loginRequestDTO;
    private User user;
    private String token;

    @BeforeEach
    void setUp() {
        loginRequestDTO = AuthFactory.createLoginRequestDTO();
        user = UserFactory.createUser();
        token = "jwt_token";
    }

    @Test
    @DisplayName("Deve autenticar usuário com sucesso e retornar DTO com token")
    void shouldAuthenticateUserSuccessfully() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(tokenService.generateToken(user)).thenReturn(token);

        // Act
        LoginResponseDTO result = authenticateUserUseCase.execute(loginRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(token, result.token());
        assertEquals(user.getEmail(), result.user().email());
        assertEquals(user.getId(), result.user().id());
        assertTrue(result.user().roles().contains("ROLE_MEMBER"));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService, times(1)).generateToken(user);
    }

    @Test
    @DisplayName("Deve lançar BadCredentialsException quando as credenciais forem inválidas")
    void shouldThrowExceptionWhenCredentialsAreInvalid() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authenticateUserUseCase.execute(loginRequestDTO));
        
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService, never()).generateToken(any());
    }
}
