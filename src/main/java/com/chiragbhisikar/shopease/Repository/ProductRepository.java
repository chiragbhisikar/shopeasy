package com.chiragbhisikar.shopease.Repository;

import com.chiragbhisikar.shopease.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByCategory_IdAndCategoryType_Id(UUID categoryId, UUID categoryTypeId);

    List<Product> findByCategory_Id(UUID categoryId);

    List<Product> findByCategoryType_Id(UUID categoryTypeId);

    Optional<Product> findBySlug(String slug);
}