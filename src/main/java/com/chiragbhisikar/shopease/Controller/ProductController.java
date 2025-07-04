package com.chiragbhisikar.shopease.Controller;

import com.chiragbhisikar.shopease.DTO.ProductDTO;
import com.chiragbhisikar.shopease.Exception.NotFoundException;
import com.chiragbhisikar.shopease.Model.Product;
import com.chiragbhisikar.shopease.Response.ApiResponse;
import com.chiragbhisikar.shopease.Services.ProductService.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
@CrossOrigin("http://localhost:3000/")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllProducts(@RequestParam(required = false, name = "categoryId", value = "categoryId") String categoryId,
                                                      @RequestParam(required = false, name = "categoryTypeId", value = "categoryTypeId") String categoryTypeId,
                                                      @RequestParam(required = false) String slug) {
        try {
            if (StringUtils.isNotBlank(slug)) {
                ProductDTO product = productService.getProductBySlug(slug);
                return new ResponseEntity<>(new ApiResponse("Product Gotten Successfully !", product), HttpStatus.OK);
            }
            List<ProductDTO> products = productService.getAllProduct(categoryId, categoryTypeId);
            return new ResponseEntity<>(new ApiResponse("Product Gotten Successfully !", products), HttpStatus.OK);
        } catch (NotFoundException ne) {
            return new ResponseEntity<>(new ApiResponse("Product Not Found !", null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("Something Went Wrong !", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> getProductDetails(@PathVariable UUID productId) {
        try {
            ProductDTO product = productService.getProduct(productId);
            return new ResponseEntity<>(new ApiResponse("Product Gotten Successfully !", product), HttpStatus.OK);
        } catch (NotFoundException ne) {
            return new ResponseEntity<>(new ApiResponse("Product Not Found !", null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("Something Went Wrong !", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductDTO reqProduct) {
        try {
            Product newProduct = productService.addProduct(reqProduct);
            return new ResponseEntity<>(new ApiResponse("Product Added Successfully !", newProduct), HttpStatus.CREATED);
        } catch (NotFoundException ne) {
            return new ResponseEntity<>(new ApiResponse("Category Not Found !", null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("Something Went Wrong !", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductDTO productDto, @PathVariable UUID productId) {
        Product product = productService.updateProduct(productDto, productId);
        return new ResponseEntity<>(new ApiResponse("Product Updated Successfully !", product), HttpStatus.OK);
    }
}
