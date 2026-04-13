package com.matheustorres.gympass.web.controllers;

import com.matheustorres.gympass.domain.models.Gym;
import com.matheustorres.gympass.domain.models.enums.UserRole;
import com.matheustorres.gympass.web.dtos.request.CheckInCreateRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CheckInControllerIntegrationTest extends AbstractIT {

    @Test
    @DisplayName("Deve fazer check-in com sucesso")
    void shouldCreateCheckInSuccessfully() throws Exception {
        createAndRegisterUser("John", "john@example.com", "password123", UserRole.MEMBER);
        String token = login("john@example.com", "password123");

        Gym gym = Gym.builder()
                .id(UUID.randomUUID().toString())
                .title("JS Academy")
                .latitude(new BigDecimal("-27.2092052"))
                .longitude(new BigDecimal("-49.6401091"))
                .build();
        gymRepository.save(gym);

        CheckInCreateRequestDTO checkInDTO = new CheckInCreateRequestDTO(-27.2092052, -49.6401091);

        mockMvc.perform(post("/check-ins/" + gym.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkInDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("Não deve fazer check-in em academia distante")
    void shouldNotCreateCheckInOnDistantGym() throws Exception {
        createAndRegisterUser("John", "john@example.com", "password123", UserRole.MEMBER);
        String token = login("john@example.com", "password123");

        Gym gym = Gym.builder()
                .id(UUID.randomUUID().toString())
                .title("JS Academy")
                .latitude(new BigDecimal("-27.2092052"))
                .longitude(new BigDecimal("-49.6401091"))
                .build();
        gymRepository.save(gym);

        // Coordenada distante
        CheckInCreateRequestDTO checkInDTO = new CheckInCreateRequestDTO(-27.0702174, -49.4812472);

        mockMvc.perform(post("/check-ins/" + gym.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkInDTO)))
                .andExpect(status().isForbidden()); // MaxDistanceException -> 403
    }

    @Test
    @DisplayName("Deve validar check-in com sucesso (Admin)")
    void shouldValidateCheckInByAdmin() throws Exception {
        // Criando admin
        createAndRegisterUser("Admin", "admin@example.com", "password123", UserRole.ADMIN);
        String token = login("admin@example.com", "password123");

        // Criando um check-in para validar
        Gym gym = Gym.builder()
                .id(UUID.randomUUID().toString())
                .title("Gym")
                .latitude(new BigDecimal("0"))
                .longitude(new BigDecimal("0"))
                .build();
        gymRepository.save(gym);

        com.matheustorres.gympass.domain.models.CheckIn checkIn = com.matheustorres.gympass.domain.models.CheckIn.builder()
                .id(UUID.randomUUID().toString())
                .user(userRepository.findAll().get(0)) // O admin mesmo ou o base
                .gym(gym)
                .createdAt(java.time.LocalDateTime.now())
                .build();
        checkInRepository.save(checkIn);

        mockMvc.perform(patch("/check-ins/validate/" + checkIn.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.validatedAt").exists());
    }

    @Test
    @DisplayName("Não deve validar check-in se usuário não for Admin")
    void shouldNotValidateCheckInByMember() throws Exception {
        createAndRegisterUser("Member", "member@example.com", "password123", UserRole.MEMBER);
        String token = login("member@example.com", "password123");

        mockMvc.perform(patch("/check-ins/validate/" + UUID.randomUUID()))
                .andExpect(status().isForbidden());
    }
}
