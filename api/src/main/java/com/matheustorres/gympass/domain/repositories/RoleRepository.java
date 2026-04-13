package com.matheustorres.gympass.domain.repositories;

import com.matheustorres.gympass.domain.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
