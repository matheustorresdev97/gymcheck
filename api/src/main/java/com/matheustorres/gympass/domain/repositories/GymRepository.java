package com.matheustorres.gympass.domain.repositories;

import com.matheustorres.gympass.domain.models.Gym;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GymRepository extends JpaRepository<Gym, String> {
}
