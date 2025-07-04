package com.chiragbhisikar.shopease.DTO;

import com.chiragbhisikar.shopease.Model.Product;
import com.chiragbhisikar.shopease.Model.ProductVariant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDTO {
    private UUID id;
    private Product product;
    private ProductVariant productVariant;
    private Integer quantity;
    private Double itemPrice;
}
