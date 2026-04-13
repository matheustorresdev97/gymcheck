package com.matheustorres.gympass.domain.usecases;

import com.matheustorres.gympass.domain.exceptions.LateCheckInValidationException;
import com.matheustorres.gympass.domain.models.CheckIn;
import com.matheustorres.gympass.domain.repositories.CheckInRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidateCheckInUseCaseTest {

    @InjectMocks
    private ValidateCheckInUseCase validateCheckInUseCase;

    @Mock
    private CheckInRepository checkInRepository;

    @Test
    @DisplayName("Deve ser possível validar o check-in")
    void shouldBeAbleToValidateCheckIn() {
        // Arrange
        CheckIn checkIn = CheckIn.builder()
                .id("check-in-1")
                .createdAt(LocalDateTime.now())
                .build();

        when(checkInRepository.findById("check-in-1")).thenReturn(Optional.of(checkIn));
        when(checkInRepository.save(any(CheckIn.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        CheckIn result = validateCheckInUseCase.execute("check-in-1");

        // Assert
        assertNotNull(result.getValidatedAt());
        verify(checkInRepository, times(1)).save(checkIn);
    }

    @Test
    @DisplayName("Não deve ser possível validar o check-in após 20 minutos de sua criação")
    void shouldNotBeAbleToValidateCheckInAfter20Minutes() {
        // Arrange
        CheckIn checkIn = CheckIn.builder()
                .id("check-in-1")
                .createdAt(LocalDateTime.now().minusMinutes(21))
                .build();

        when(checkInRepository.findById("check-in-1")).thenReturn(Optional.of(checkIn));

        // Act & Assert
        assertThrows(LateCheckInValidationException.class, () -> 
                validateCheckInUseCase.execute("check-in-1"));
        verify(checkInRepository, never()).save(any(CheckIn.class));
    }
}
