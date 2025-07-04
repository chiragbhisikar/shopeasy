package com.chiragbhisikar.shopease.Auth.Repository;

import com.chiragbhisikar.shopease.Model.Auth.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorityRepository extends JpaRepository<Authority, UUID> {
    Authority findAuthorityByRoleCode(String roleCode);
}
