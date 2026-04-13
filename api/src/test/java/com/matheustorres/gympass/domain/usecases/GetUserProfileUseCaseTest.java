package com.matheustorres.gympass.domain.usecases;

import com.matheustorres.gympass.domain.exceptions.ResourceNotFoundException;
import com.matheustorres.gympass.domain.models.User;
import com.matheustorres.gympass.domain.repositories.UserRepository;
import com.matheustorres.gympass.tests.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUserProfileUseCaseTest {

    @InjectMocks
    private GetUserProfileUseCase getUserProfileUseCase;

    @Mock
    private UserRepository userRepository;

    private User user;
    private String userId;

    @BeforeEach
    void setUp() {
        user = UserFactory.createUser();
        userId = user.getId();
    }

    @Test
    @DisplayName("Deve retornar o perfil do usuário quando o ID existe")
    void shouldReturnUserProfileWhenIdExists() {
        // Arrange
        when(userRepository.findByIdWithRoles(userId)).thenReturn(Optional.of(user));

        // Act
        User result = getUserProfileUseCase.execute(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findByIdWithRoles(userId);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando o ID não existe")
    void shouldThrowExceptionWhenIdDoesNotExist() {
        // Arrange
        when(userRepository.findByIdWithRoles(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> getUserProfileUseCase.execute(userId));
        verify(userRepository, times(1)).findByIdWithRoles(userId);
    }
}
