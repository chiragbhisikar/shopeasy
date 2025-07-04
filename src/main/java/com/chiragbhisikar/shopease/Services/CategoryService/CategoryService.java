package com.chiragbhisikar.shopease.Services.CategoryService;

import com.chiragbhisikar.shopease.DTO.CategoryDTO;
import com.chiragbhisikar.shopease.DTO.CategoryTypeDTO;
import com.chiragbhisikar.shopease.Exception.NotFoundException;
import com.chiragbhisikar.shopease.Model.Category;
import com.chiragbhisikar.shopease.Model.CategoryType;
import com.chiragbhisikar.shopease.Repository.CategoryRepository;
import com.chiragbhisikar.shopease.Services.CategoryTypeService.CategoryTypeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CategoryService implements iCategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryTypeService categoryTypeService;

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategory(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category Not Found"));
    }

    public Category addCategory(CategoryDTO reqCategory) {
        Category category = convertCategoryDtoToEntity(reqCategory);
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(CategoryDTO reqCategory, UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category Not Found !"));

        if (null != reqCategory.getName()) {
            category.setName(reqCategory.getName());
        }
        if (null != reqCategory.getCode()) {
            category.setCode(reqCategory.getCode());
        }
        if (null != reqCategory.getDescription()) {
            category.setDescription(reqCategory.getDescription());
        }

        List<CategoryType> updatedCategoryTypes = new ArrayList<>();
        List<CategoryType> existingCategoryTypes = category.getCategoryTypes();

        if (reqCategory.getCategoryTypes() != null) {
            for (CategoryTypeDTO categoryTypeDTO : reqCategory.getCategoryTypes()) {
                CategoryType categoryType;

                if (categoryTypeDTO.getId() != null) {
                    // Update existing type
                    categoryType = existingCategoryTypes.stream()
                            .filter(tempCategoryType -> tempCategoryType.getId().equals(categoryTypeDTO.getId()))
                            .findFirst()
                            .orElse(new CategoryType());
                } else {
                    // Create new categoryType
                    categoryType = new CategoryType();
                }

                categoryType.setCategory(category);
                categoryType.setName(categoryTypeDTO.getName());
                categoryType.setCode(categoryTypeDTO.getCode());
                categoryType.setDescription(categoryTypeDTO.getDescription());

                updatedCategoryTypes.add(categoryType);
            }
        }
        category.setCategoryTypes(updatedCategoryTypes);

        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(UUID categoryId) {
        categoryRepository.deleteById(categoryId);
    }


    @Override
    public Category convertCategoryDtoToEntity(CategoryDTO categoryDTO) {
        Category category = Category.builder()
                .code(categoryDTO.getCode())
                .name(categoryDTO.getName())
                .description(categoryDTO.getDescription())
                .build();

        if (null != categoryDTO.getCategoryTypes()) {
            List<CategoryType> categoryTypes = categoryTypeService.convertCategoryTypesDtoToEntities(categoryDTO.getCategoryTypes(), category);
            category.setCategoryTypes(categoryTypes);
        }

        return category;
    }
}
