package com.chiragbhisikar.shopease.DTO;

import lombok.Data;

import java.util.UUID;

@Data
public class CategoryTypeDTO {
    private UUID id;
    private String name;
    private String code;
    private String description;
}
