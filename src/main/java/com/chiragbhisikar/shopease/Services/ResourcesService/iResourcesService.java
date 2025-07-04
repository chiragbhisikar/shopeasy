package com.chiragbhisikar.shopease.Services.ResourcesService;

import com.chiragbhisikar.shopease.DTO.ResourceDTO;
import com.chiragbhisikar.shopease.Model.Product;
import com.chiragbhisikar.shopease.Model.Resources;

import java.util.List;
import java.util.UUID;

public interface iResourcesService {
    public List<Resources> getAllResources();

    public Resources getResources(UUID ResourcesId);

    public List<ResourceDTO> mapResourcesListDto(List<Resources> resources);

    public Resources convertResourcesDtoToEntity(ResourceDTO resource, Product product);

    public List<Resources> convertResourcesDtoToEntities(List<ResourceDTO> resourcesList, Product product);

    public String getProductThumbnail(List<Resources> resources);
}
