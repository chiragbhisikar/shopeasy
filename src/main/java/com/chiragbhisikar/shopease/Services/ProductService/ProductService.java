package com.chiragbhisikar.shopease.Services.ProductService;

import com.chiragbhisikar.shopease.DTO.ProductDTO;
import com.chiragbhisikar.shopease.Exception.NotFoundException;
import com.chiragbhisikar.shopease.Model.Category;
import com.chiragbhisikar.shopease.Model.CategoryType;
import com.chiragbhisikar.shopease.Model.Product;
import com.chiragbhisikar.shopease.Repository.ProductRepository;
import com.chiragbhisikar.shopease.Services.CategoryService.CategoryService;
import com.chiragbhisikar.shopease.Services.CategoryTypeService.CategoryTypeService;
import com.chiragbhisikar.shopease.Services.ProductVariantService.ProductVariantService;
import com.chiragbhisikar.shopease.Services.ResourcesService.ResourcesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService implements iProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final CategoryTypeService categoryTypeService;
    private final ProductVariantService productVariantService;
    private final ResourcesService resourcesService;

    @Override
    public List<ProductDTO> getAllProduct(String categoryId, String categoryTypeId) {
        UUID categoryUUID = categoryId != null ? UUID.fromString(categoryId) : null;
        UUID categoryTypeUUID = categoryTypeId != null ? UUID.fromString(categoryTypeId) : null;
        List<Product> products;
        if (categoryUUID != null && categoryTypeUUID != null) {
            products = productRepository.findByCategory_IdAndCategoryType_Id(categoryUUID, categoryTypeUUID);
        } else if (categoryUUID != null) {
            products = productRepository.findByCategory_Id(categoryUUID);
        } else if (categoryTypeUUID != null) {
            products = productRepository.findByCategoryType_Id(categoryTypeUUID);
        } else {
            products = productRepository.findAll();

        }
        return this.mapProductsToDTO(products);
    }

    @Override
    public ProductDTO getProduct(UUID id) throws NotFoundException {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product Not Found"));
        return mapProductToDto(product);
    }

    @Override
    public ProductDTO getProductBySlug(String slug) throws NotFoundException {
        Product product = productRepository.findBySlug(slug).orElseThrow(() -> new NotFoundException("Product Not Found"));
        return mapProductToDto(product);
    }

    @Override
    public Product addProduct(ProductDTO reqProduct) {
        Product newProduct = this.mapToProductEntity(reqProduct);
        return productRepository.save(newProduct);
    }

    @Override
    public Product updateProduct(ProductDTO productDto, UUID id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product Not Found!"));
        productDto.setId(product.getId());
        return productRepository.save(this.mapToProductEntity(productDto));
    }

    @Override
    public List<ProductDTO> mapProductsToDTO(List<Product> products) {
        return products.stream().map(this::mapProductToDto).toList();
    }

    @Override
    public ProductDTO mapProductToDto(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .brand(product.getBrand())
                .name(product.getName())
                .price(product.getPrice())
                .isNewArrival(product.getIsNewArrival())
                .categoryId(product.getCategory().getId())
                .categoryTypeId(product.getCategoryType().getId())
                .rating(product.getRating())
                .description(product.getDescription())
                .slug(product.getSlug())
                .categoryName(product.getCategory().getName())
                .categoryTypeName(product.getCategoryType().getName())
                .variants(productVariantService.mapProductVariantListToDto(product.getProductVariants()))
                .resources(resourcesService.mapResourcesListDto(product.getResources()))
                .thumbnail(resourcesService.getProductThumbnail(product.getResources())).build();
    }

    public Product mapToProductEntity(ProductDTO productDto) {
        Product newProduct = new Product();
        if (null != productDto.getId()) {
            newProduct.setId(productDto.getId());
        }
        newProduct.setName(productDto.getName());
        newProduct.setDescription(productDto.getDescription());
        newProduct.setBrand(productDto.getBrand());
        newProduct.setIsNewArrival(productDto.getIsNewArrival());
        newProduct.setPrice(productDto.getPrice());
        newProduct.setRating(productDto.getRating());
        newProduct.setSlug(productDto.getSlug());


        Category category = categoryService.getCategory(productDto.getCategoryId());
        if (null != category) {
            newProduct.setCategory(category);
            UUID categoryTypeId = productDto.getCategoryTypeId();

            CategoryType categoryType = category.getCategoryTypes().stream().filter(categoryType1 -> categoryType1.getId().equals(categoryTypeId)).findFirst().orElse(null);
            newProduct.setCategoryType(categoryType);
        }

        if (null != productDto.getVariants()) {
            newProduct.setProductVariants(productVariantService.convertProductVariantDtoToEntities(productDto.getVariants(), newProduct));
        }

        if (null != productDto.getResources()) {
            newProduct.setResources(resourcesService.convertResourcesDtoToEntities(productDto.getResources(), newProduct));
        }

        return newProduct;
    }


    public Product findProductById(UUID id) throws NotFoundException {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product Not Found"));
    }
}
