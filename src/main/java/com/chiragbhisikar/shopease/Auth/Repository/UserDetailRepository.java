package com.chiragbhisikar.shopease.Auth.Repository;

import com.chiragbhisikar.shopease.Model.Auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDetailRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String username);
}
