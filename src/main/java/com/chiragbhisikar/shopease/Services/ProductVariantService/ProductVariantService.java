package com.chiragbhisikar.shopease.Services.ProductVariantService;

import com.chiragbhisikar.shopease.DTO.ProductVariantDTO;
import com.chiragbhisikar.shopease.Exception.NotFoundException;
import com.chiragbhisikar.shopease.Model.Product;
import com.chiragbhisikar.shopease.Model.ProductVariant;
import com.chiragbhisikar.shopease.Repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductVariantService implements iProductVariantService {
    private final ProductVariantRepository ProductVariantRepository;

    @Override
    public List<ProductVariant> getAllProductVariant() {
        return ProductVariantRepository.findAll();
    }

    @Override
    public ProductVariant getProductVariant(UUID id) {
        return ProductVariantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ProductVariant Not Found"));
    }


    @Override
    public List<ProductVariantDTO> mapProductVariantListToDto(List<ProductVariant> productVariants) {
        return productVariants.stream().map(this::mapProductVariantDto).toList();
    }

    private ProductVariantDTO mapProductVariantDto(ProductVariant productVariant) {
        return ProductVariantDTO.builder()
                .color(productVariant.getColor())
                .id(productVariant.getId())
                .size(productVariant.getSize())
                .stockQuantity(productVariant.getStockQuantity())
                .build();
    }

    @Override
    public ProductVariant convertProductVariantDtoToEntity(ProductVariantDTO productVariantDto, Product product) {
        ProductVariant productVariant = new ProductVariant();
        if (null != productVariantDto.getId()) {
            productVariant.setId(productVariantDto.getId());
        }
        productVariant.setColor(productVariantDto.getColor());
        productVariant.setSize(productVariantDto.getSize());
        productVariant.setStockQuantity(productVariantDto.getStockQuantity());
        productVariant.setProduct(product);
        return productVariant;
    }


    @Override
    public List<ProductVariant> convertProductVariantDtoToEntities(List<ProductVariantDTO> productVariantDTOList, Product product) {
        return productVariantDTOList.stream().map(productVariantDto -> convertProductVariantDtoToEntity(productVariantDto, product)).collect(Collectors.toList());
    }
}
