package com.chiragbhisikar.shopease.Services.CategoryTypeService;

import com.chiragbhisikar.shopease.DTO.CategoryTypeDTO;
import com.chiragbhisikar.shopease.Exception.NotFoundException;
import com.chiragbhisikar.shopease.Model.Category;
import com.chiragbhisikar.shopease.Model.CategoryType;
import com.chiragbhisikar.shopease.Repository.CategoryTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CategoryTypeService implements iCategoryTypeService {
    private final CategoryTypeRepository categoryTypeRepository;

    @Override
    public List<CategoryType> getAllCategoryType() {
        return categoryTypeRepository.findAll();
    }

    @Override
    public CategoryType getCategoryType(UUID id) {
        return categoryTypeRepository.findById(id).orElseThrow(() -> new NotFoundException("CategoryType Not Found !"));
    }

    @Override
    public CategoryType addCategoryType(CategoryTypeDTO reqCategoryType) {
        return null;
    }

    @Override
    public CategoryType convertCategoryTypeDtoToEntity(CategoryTypeDTO categoryTypeDTO, Category category) {
        CategoryType categoryType = new CategoryType();
        categoryType.setCode(categoryTypeDTO.getCode());
        categoryType.setName(categoryTypeDTO.getName());
        categoryType.setDescription(categoryTypeDTO.getDescription());
        categoryType.setCategory(category);
        return categoryType;
    }


    @Override
    public List<CategoryType> convertCategoryTypesDtoToEntities(List<CategoryTypeDTO> categoryTypeList, Category category) {
        return categoryTypeList.stream().map(categoryTypeDto -> convertCategoryTypeDtoToEntity(categoryTypeDto, category)).collect(Collectors.toList());
    }
}
