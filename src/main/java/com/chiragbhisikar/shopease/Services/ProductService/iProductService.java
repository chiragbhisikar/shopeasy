package com.chiragbhisikar.shopease.Services.ProductService;

import com.chiragbhisikar.shopease.DTO.ProductDTO;
import com.chiragbhisikar.shopease.Model.Product;

import java.util.List;
import java.util.UUID;

public interface iProductService {
    public List<ProductDTO> getAllProduct(String categoryId, String categoryTypeId);

    public ProductDTO getProduct(UUID id);

    public ProductDTO getProductBySlug(String slug);

    public Product addProduct(ProductDTO reqProduct);

    public Product updateProduct(ProductDTO productDto, UUID id);

    public List<ProductDTO> mapProductsToDTO(List<Product> products);

    public ProductDTO mapProductToDto(Product product);
}
