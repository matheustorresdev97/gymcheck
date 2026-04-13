package com.matheustorres.gympass.domain.usecases;

import com.matheustorres.gympass.domain.models.Gym;
import com.matheustorres.gympass.domain.repositories.GymRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchGymsUseCaseTest {

    @InjectMocks
    private SearchGymsUseCase searchGymsUseCase;

    @Mock
    private GymRepository gymRepository;

    @Test
    @DisplayName("Deve ser possível buscar academias pelo título")
    void shouldBeAbleToSearchGymsByTitle() {
        // Arrange
        String query = "JS";
        Pageable pageable = PageRequest.of(0, 20);
        Page<Gym> page = new PageImpl<>(List.of(new Gym(), new Gym()));
        
        when(gymRepository.findByTitleContainingIgnoreCase(query, pageable)).thenReturn(page);

        // Act
        Page<Gym> result = searchGymsUseCase.execute(query, pageable);

        // Assert
        assertEquals(2, result.getTotalElements());
        verify(gymRepository, times(1)).findByTitleContainingIgnoreCase(query, pageable);
    }
}
