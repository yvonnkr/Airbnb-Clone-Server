package com.yvolabs.airbnbclone.user.repository;

import com.yvolabs.airbnbclone.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 17/09/2024
 */

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByEmail(String email);
    Optional<User> findOneByPublicId(UUID publicId);
}
