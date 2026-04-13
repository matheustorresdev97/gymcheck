package com.matheustorres.gympass.domain.usecases;

import com.matheustorres.gympass.domain.models.Gym;
import com.matheustorres.gympass.domain.repositories.GymRepository;
import com.matheustorres.gympass.web.dtos.request.GymRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateGymUseCaseTest {

    @InjectMocks
    private CreateGymUseCase createGymUseCase;

    @Mock
    private GymRepository gymRepository;

    @Test
    @DisplayName("Deve criar uma academia com sucesso")
    void shouldCreateGymSuccessfully() {
        // Arrange
        GymRequestDTO dto = new GymRequestDTO(
                "JS Academy",
                "The best gym",
                "12345678",
                -27.2092052,
                -49.6401091
        );

        when(gymRepository.save(any(Gym.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Gym result = createGymUseCase.execute(dto);

        // Assert
        assertNotNull(result);
        assertEquals(dto.title(), result.getTitle());
        assertNotNull(result.getId());
        verify(gymRepository, times(1)).save(any(Gym.class));
    }
}
