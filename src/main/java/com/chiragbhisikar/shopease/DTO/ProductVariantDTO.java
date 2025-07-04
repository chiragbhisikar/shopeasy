package com.chiragbhisikar.shopease.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantDTO {
    private UUID id;
    private String color;
    private String size;
    private Integer stockQuantity;

}
