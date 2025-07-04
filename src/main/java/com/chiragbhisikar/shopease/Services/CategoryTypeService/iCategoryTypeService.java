package com.chiragbhisikar.shopease.Services.CategoryTypeService;

import com.chiragbhisikar.shopease.DTO.CategoryTypeDTO;
import com.chiragbhisikar.shopease.Model.Category;
import com.chiragbhisikar.shopease.Model.CategoryType;

import java.util.List;
import java.util.UUID;

public interface iCategoryTypeService {
    public List<CategoryType> getAllCategoryType();

    public CategoryType getCategoryType(UUID id);

    public CategoryType addCategoryType(CategoryTypeDTO reqCategoryType);

    public CategoryType convertCategoryTypeDtoToEntity(CategoryTypeDTO categoryTypeDTO, Category category);

    public List<CategoryType> convertCategoryTypesDtoToEntities(List<CategoryTypeDTO> categoryTypeList, Category category);

}
