package com.matheustorres.gympass.domain.repositories;

import com.matheustorres.gympass.domain.models.Gym;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GymRepository extends JpaRepository<Gym, String> {
    Page<Gym> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
