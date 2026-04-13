package com.matheustorres.gympass.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matheustorres.gympass.domain.models.Role;
import com.matheustorres.gympass.domain.models.User;
import com.matheustorres.gympass.domain.models.enums.UserRole;
import com.matheustorres.gympass.domain.repositories.CheckInRepository;
import com.matheustorres.gympass.domain.repositories.GymRepository;
import com.matheustorres.gympass.domain.repositories.RoleRepository;
import com.matheustorres.gympass.domain.repositories.UserRepository;
import com.matheustorres.gympass.web.dtos.request.LoginRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public abstract class AbstractIT {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected RoleRepository roleRepository;

    @Autowired
    protected GymRepository gymRepository;

    @Autowired
    protected CheckInRepository checkInRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @BeforeEach
    void clearDatabase() {
        checkInRepository.deleteAll();
        gymRepository.deleteAll();
        userRepository.deleteAll();
    }

    protected String login(String email, String password) throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO(email, password);
        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        return objectMapper.readTree(response).get("token").asText();
    }

    protected User createAndRegisterUser(String name, String email, String password, UserRole roleType) {
        Role role = roleRepository.findByAuthority(roleType.getRole())
                .orElseGet(() -> roleRepository.save(new Role(null, roleType.getRole())));

        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .name(name)
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .roles(Set.of(role))
                .build();

        return userRepository.save(user);
    }
}
