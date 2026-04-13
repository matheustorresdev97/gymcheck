package com.matheustorres.gympass.domain.repositories;

import com.matheustorres.gympass.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, String> {
    UserDetails findByEmail(String email);
}
