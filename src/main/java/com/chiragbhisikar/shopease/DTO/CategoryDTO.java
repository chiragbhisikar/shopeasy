package com.chiragbhisikar.shopease.DTO;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CategoryDTO {
    private UUID id;

    private String name;

    private String code;

    private String description;

    private List<CategoryTypeDTO> categoryTypes;
}
