package com.chiragbhisikar.shopease.Services.CategoryService;

import com.chiragbhisikar.shopease.DTO.CategoryDTO;
import com.chiragbhisikar.shopease.Model.Category;

import java.util.List;
import java.util.UUID;

public interface iCategoryService {
    public List<Category> getAllCategory();

    public Category getCategory(UUID categoryId);

    public Category addCategory(CategoryDTO reqCategory);

    public Category updateCategory(CategoryDTO reqCategory, UUID categoryId);

    public void deleteCategory(UUID categoryId);

    public Category convertCategoryDtoToEntity(CategoryDTO categoryDTO);
}
