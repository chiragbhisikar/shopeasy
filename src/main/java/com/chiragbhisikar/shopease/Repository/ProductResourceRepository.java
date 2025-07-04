package com.chiragbhisikar.shopease.Repository;


import com.chiragbhisikar.shopease.Model.Resources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductResourceRepository extends JpaRepository<Resources, UUID> {
}