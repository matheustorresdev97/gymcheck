package com.matheustorres.gympass.domain.usecases;

import com.matheustorres.gympass.domain.repositories.CheckInRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUserMetricsUseCaseTest {

    @InjectMocks
    private GetUserMetricsUseCase getUserMetricsUseCase;

    @Mock
    private CheckInRepository checkInRepository;

    @Test
    @DisplayName("Caso de uso: Obter métricas do usuário")
    void shouldBeAbleToGetUserMetrics() {
        // Arrange
        String userId = "user-id-1";
        when(checkInRepository.countByUserId(userId)).thenReturn(5L);

        // Act
        long result = getUserMetricsUseCase.execute(userId);

        // Assert
        assertEquals(5L, result);
        verify(checkInRepository, times(1)).countByUserId(userId);
    }

    @Test
    @DisplayName("deve ser possível obter a contagem de check-ins a partir das métricas")
    void shouldBeAbleToGetCheckInsCountFromMetrics() {
        // Arrange
        String userId = "user-id-2";
        when(checkInRepository.countByUserId(userId)).thenReturn(10L);

        // Act
        long count = getUserMetricsUseCase.execute(userId);

        // Assert
        assertEquals(10L, count);
        verify(checkInRepository, times(1)).countByUserId(userId);
    }
}
