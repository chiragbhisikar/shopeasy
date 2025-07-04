package com.chiragbhisikar.shopease.Controller;

import com.chiragbhisikar.shopease.DTO.CategoryDTO;
import com.chiragbhisikar.shopease.Exception.NotFoundException;
import com.chiragbhisikar.shopease.Model.Category;
import com.chiragbhisikar.shopease.Response.ApiResponse;
import com.chiragbhisikar.shopease.Services.CategoryService.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
@CrossOrigin("http://localhost:3000/")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllCategories() {
        List<Category> categories = categoryService.getAllCategory();
        return new ResponseEntity<>(new ApiResponse("Category Gotten Successfully !", categories), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getCategoryDetails(@PathVariable(value = "id", required = true) UUID categoryId) {
        try {
            Category Category = categoryService.getCategory(categoryId);
            return new ResponseEntity<>(new ApiResponse("Category Gotten Successfully !", Category), HttpStatus.OK);
        } catch (NotFoundException ne) {
            return new ResponseEntity<>(new ApiResponse("Category Not Found !", null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("Something Went Wrong ! " + e.toString(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addCategory(@RequestBody CategoryDTO reqCategory) {
        try {
            Category newCategory = categoryService.addCategory(reqCategory);
            return new ResponseEntity<>(new ApiResponse("Category Added Successfully !", newCategory), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("Something Went Wrong ! " + e.toString(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable(value = "id", required = true) UUID categoryId, @RequestBody CategoryDTO reqCategory) {
        try {
            Category updatedCategory = categoryService.updateCategory(reqCategory, categoryId);
            return new ResponseEntity<>(new ApiResponse("Category Added Successfully !", updatedCategory), HttpStatus.ACCEPTED);
        } catch (NotFoundException ne) {
            return new ResponseEntity<>(new ApiResponse("Category Not Found !", null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("Something Went Wrong ! " + e.toString(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable(value = "id", required = true) UUID categoryId) {
        try {
            categoryService.deleteCategory(categoryId);
            return new ResponseEntity<>(new ApiResponse("Category Deleted Successfully !", null), HttpStatus.ACCEPTED);
        } catch (NotFoundException ne) {
            return new ResponseEntity<>(new ApiResponse("Category Not Found !", null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("Something Went Wrong ! " + e.toString(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
