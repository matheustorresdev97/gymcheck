package com.matheustorres.gympass.web.controllers;

import com.matheustorres.gympass.domain.models.enums.UserRole;
import com.matheustorres.gympass.web.dtos.request.GymRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GymControllerIntegrationTest extends AbstractIT {

    @Test
    @DisplayName("Deve criar uma academia com sucesso (Admin)")
    void shouldCreateGymSuccessfullyByAdmin() throws Exception {
        createAndRegisterUser("Admin", "admin@example.com", "password123", UserRole.ADMIN);
        String token = login("admin@example.com", "password123");

        GymRequestDTO gymDTO = new GymRequestDTO("JS Academy", "Best academy", "1199999999", -27.2092052, -49.6401091);

        mockMvc.perform(post("/gyms")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gymDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("JS Academy"));
    }

    @Test
    @DisplayName("Não deve criar academia se usuário não for Admin")
    void shouldNotCreateGymByMember() throws Exception {
        createAndRegisterUser("Member", "member-test@example.com", "password123", UserRole.MEMBER);
        String token = login("member-test@example.com", "password123");

        GymRequestDTO gymDTO = new GymRequestDTO("JS Academy", "Best academy", "1199999999", -27.2092052, -49.6401091);

        mockMvc.perform(post("/gyms")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gymDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve buscar academias pelo título (Público)")
    void shouldSearchGymsByTitle() throws Exception {
        // Sem necessidade de login ja que a rota e publica
        GymRequestDTO gym1 = new GymRequestDTO("JS Academy", "Best academy", "1199999999", -27.2092052, -49.6401091);
        
        // Cadastrando via repositorio para agilizar
        com.matheustorres.gympass.domain.models.Gym gym = com.matheustorres.gympass.domain.models.Gym.builder()
                .id(java.util.UUID.randomUUID().toString())
                .title("JS Academy")
                .description("Desc")
                .phone("111")
                .latitude(java.math.BigDecimal.valueOf(-27.2092052))
                .longitude(java.math.BigDecimal.valueOf(-49.6401091))
                .build();
        gymRepository.save(gym);

        mockMvc.perform(get("/gyms/search")
                        .param("q", "JS")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("JS Academy"));
    }
}
