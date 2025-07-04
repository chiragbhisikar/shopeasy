package com.chiragbhisikar.shopease.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private UUID id;
    private String name;
    private String description;
    private Double price;
    private String brand;
    private Float rating;
    private Boolean isNewArrival;
    private String thumbnail;
    private String slug;
    private UUID categoryId;
    private String categoryName;
    private UUID categoryTypeId;
    private String categoryTypeName;
    private List<ProductVariantDTO> variants;
    private List<ResourceDTO> resources;

}
