package com.matheustorres.gympass.domain.usecases;

import com.matheustorres.gympass.domain.exceptions.ResourceNotFoundException;
import com.matheustorres.gympass.domain.models.CheckIn;
import com.matheustorres.gympass.domain.models.Gym;
import com.matheustorres.gympass.domain.models.User;
import com.matheustorres.gympass.domain.repositories.CheckInRepository;
import com.matheustorres.gympass.domain.repositories.GymRepository;
import com.matheustorres.gympass.domain.repositories.UserRepository;
import com.matheustorres.gympass.tests.GymFactory;
import com.matheustorres.gympass.tests.UserFactory;
import com.matheustorres.gympass.web.dtos.request.CheckInRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCheckInUseCaseTest {

    @InjectMocks
    private CreateCheckInUseCase createCheckInUseCase;

    @Mock
    private CheckInRepository checkInRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GymRepository gymRepository;

    private User user;
    private Gym gym;
    private CheckInRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        user = UserFactory.createUser();
        gym = GymFactory.createGym();
        requestDTO = new CheckInRequestDTO(user.getId(), gym.getId());
    }

    @Test
    @DisplayName("Deve criar um check-in com sucesso")
    void shouldCreateCheckInSuccessfully() {
        // Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(gymRepository.findById(gym.getId())).thenReturn(Optional.of(gym));
        when(checkInRepository.save(any(CheckIn.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        CheckIn result = createCheckInUseCase.execute(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(user.getId(), result.getUser().getId());
        assertEquals(gym.getId(), result.getGym().getId());
        assertNotNull(result.getId());
        verify(userRepository, times(1)).findById(user.getId());
        verify(gymRepository, times(1)).findById(gym.getId());
        verify(checkInRepository, times(1)).save(any(CheckIn.class));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando o usuário não existe")
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> createCheckInUseCase.execute(requestDTO));
        verify(userRepository, times(1)).findById(user.getId());
        verifyNoInteractions(gymRepository, checkInRepository);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando a academia não existe")
    void shouldThrowExceptionWhenGymNotFound() {
        // Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(gymRepository.findById(gym.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> createCheckInUseCase.execute(requestDTO));
        verify(gymRepository, times(1)).findById(gym.getId());
        verifyNoInteractions(checkInRepository);
    }
}
