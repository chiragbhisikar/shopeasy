package com.chiragbhisikar.shopease.Services.ProductVariantService;

import com.chiragbhisikar.shopease.DTO.ProductVariantDTO;
import com.chiragbhisikar.shopease.Model.Product;
import com.chiragbhisikar.shopease.Model.ProductVariant;

import java.util.List;
import java.util.UUID;

public interface iProductVariantService {
    public List<ProductVariant> getAllProductVariant();

    public ProductVariant getProductVariant(UUID ProductVariantId);

    public List<ProductVariantDTO> mapProductVariantListToDto(List<ProductVariant> productVariants);

    public ProductVariant convertProductVariantDtoToEntity(ProductVariantDTO productVariantDto, Product product);

    public List<ProductVariant> convertProductVariantDtoToEntities(List<ProductVariantDTO> productVariantDTOList, Product product);
}
