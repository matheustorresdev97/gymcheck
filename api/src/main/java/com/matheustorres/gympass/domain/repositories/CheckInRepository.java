package com.matheustorres.gympass.domain.repositories;

import com.matheustorres.gympass.domain.models.CheckIn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckInRepository extends JpaRepository<CheckIn, String> {
    Page<CheckIn> findByUserId(String userId, Pageable pageable);

    long countByUserId(String userId);
}
