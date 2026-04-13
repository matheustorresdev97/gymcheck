package com.matheustorres.gympass.domain.usecases;

import com.matheustorres.gympass.domain.models.Gym;
import com.matheustorres.gympass.domain.repositories.GymRepository;
import com.matheustorres.gympass.web.dtos.request.NearbyGymsRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FetchNearbyGymsUseCaseTest {

    @InjectMocks
    private FetchNearbyGymsUseCase fetchNearbyGymsUseCase;

    @Mock
    private GymRepository gymRepository;

    @Test
    @DisplayName("Caso de uso do recurso Buscar academias próximas, deve ser possível encontrar academias próximas")
    void shouldBeAbleToFetchNearbyGyms() {
        // Arrange
        // Academia próxima (menos de 10km)
        Gym nearGym = Gym.builder()
                .id("near")
                .title("Near Gym")
                .latitude(new BigDecimal("-27.2092052"))
                .longitude(new BigDecimal("-49.6401091"))
                .build();

        // Academia distante (mais de 10km)
        Gym farGym = Gym.builder()
                .id("far")
                .title("Far Gym")
                .latitude(new BigDecimal("-27.0702174"))
                .longitude(new BigDecimal("-49.4812472"))
                .build();

        when(gymRepository.findAll()).thenReturn(List.of(nearGym, farGym));

        NearbyGymsRequestDTO request = new NearbyGymsRequestDTO(-27.2092052, -49.6401091);

        // Act
        List<Gym> result = fetchNearbyGymsUseCase.execute(request);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Near Gym", result.get(0).getTitle());
        verify(gymRepository, times(1)).findAll();
    }
}
