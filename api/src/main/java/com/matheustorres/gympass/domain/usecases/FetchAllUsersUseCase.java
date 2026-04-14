package com.matheustorres.gympass.domain.usecases;

import com.matheustorres.gympass.domain.models.User;
import com.matheustorres.gympass.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FetchAllUsersUseCase {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<User> execute(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
