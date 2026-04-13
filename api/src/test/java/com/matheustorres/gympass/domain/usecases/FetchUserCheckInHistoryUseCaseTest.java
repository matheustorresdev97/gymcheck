package com.matheustorres.gympass.domain.usecases;

import com.matheustorres.gympass.domain.models.CheckIn;
import com.matheustorres.gympass.domain.models.Role;
import com.matheustorres.gympass.domain.models.User;
import com.matheustorres.gympass.domain.models.enums.UserRole;
import com.matheustorres.gympass.domain.repositories.CheckInRepository;
import com.matheustorres.gympass.tests.UserFactory;
import com.matheustorres.gympass.web.controllers.exceptions.ForbiddenException;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FetchUserCheckInHistoryUseCaseTest {

    @InjectMocks
    private FetchUserCheckInHistoryUseCase fetchUserCheckInHistoryUseCase;

    @Mock
    private CheckInRepository checkInRepository;

    private User member;
    private User admin;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        member = UserFactory.createUser();
        member.setRoles(Set.of(new Role(1L, UserRole.MEMBER.getRole())));

        admin = UserFactory.createUser();
        admin.setRoles(Set.of(new Role(2L, UserRole.ADMIN.getRole())));

        pageable = PageRequest.of(0, 20);
    }

    @Test
    @DisplayName("Deve ser possível obter o histórico de check-in do próprio usuário")
    void shouldBeAbleToFetchOwnCheckInHistory() {
        // Arrange
        Page<CheckIn> page = new PageImpl<>(List.of(new CheckIn()));
        when(checkInRepository.findByUserId(member.getId(), pageable)).thenReturn(page);

        // Act
        Page<CheckIn> result = fetchUserCheckInHistoryUseCase.execute(member, member.getId(), pageable);

        // Assert
        assertFalse(result.isEmpty());
        verify(checkInRepository, times(1)).findByUserId(member.getId(), pageable);
    }

    @Test
    @DisplayName("Admin deve ser possível obter o histórico de check-in de qualquer usuário")
    void adminShouldBeAbleToFetchAnyUserCheckInHistory() {
        // Arrange
        String targetUserId = "other-user-id";
        Page<CheckIn> page = new PageImpl<>(List.of(new CheckIn()));
        when(checkInRepository.findByUserId(targetUserId, pageable)).thenReturn(page);

        // Act
        Page<CheckIn> result = fetchUserCheckInHistoryUseCase.execute(admin, targetUserId, pageable);

        // Assert
        assertFalse(result.isEmpty());
        verify(checkInRepository, times(1)).findByUserId(targetUserId, pageable);
    }

    @Test
    @DisplayName("Não deve ser possível um membro obter o histórico de outro usuário")
    void memberShouldNotBeAbleToFetchOtherUserCheckInHistory() {
        // Arrange
        String targetUserId = "other-user-id";

        // Act & Assert
        assertThrows(ForbiddenException.class, () -> 
                fetchUserCheckInHistoryUseCase.execute(member, targetUserId, pageable));
        verify(checkInRepository, never()).findByUserId(anyString(), any(Pageable.class));
    }

    @Test
    @DisplayName("Admin deve ser possível obter o histórico global de check-ins")
    void adminShouldBeAbleToFetchGlobalCheckInHistory() {
        // Arrange
        Page<CheckIn> page = new PageImpl<>(List.of(new CheckIn(), new CheckIn()));
        when(checkInRepository.findAll(pageable)).thenReturn(page);

        // Act
        Page<CheckIn> result = fetchUserCheckInHistoryUseCase.execute(admin, null, pageable);

        // Assert
        assertEquals(2, result.getTotalElements());
        verify(checkInRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Deve ser possível obter o histórico de check-ins paginado")
    void shouldBeAbleToFetchPaginatedCheckInHistory() {
        // Arrange
        Pageable customPageable = PageRequest.of(1, 10);
        Page<CheckIn> page = new PageImpl<>(List.of(), customPageable, 50);
        when(checkInRepository.findByUserId(member.getId(), customPageable)).thenReturn(page);

        // Act
        Page<CheckIn> result = fetchUserCheckInHistoryUseCase.execute(member, null, customPageable);

        // Assert
        assertEquals(50, result.getTotalElements());
        assertEquals(1, result.getNumber());
        verify(checkInRepository, times(1)).findByUserId(member.getId(), customPageable);
    }
}
