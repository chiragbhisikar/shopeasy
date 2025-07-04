package com.chiragbhisikar.shopease.Services.ResourcesService;

import com.chiragbhisikar.shopease.DTO.ResourceDTO;
import com.chiragbhisikar.shopease.Exception.NotFoundException;
import com.chiragbhisikar.shopease.Model.Product;
import com.chiragbhisikar.shopease.Model.Resources;
import com.chiragbhisikar.shopease.Repository.ProductResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ResourcesService implements iResourcesService {
    private final ProductResourceRepository ResourcesRepository;

    @Override
    public List<Resources> getAllResources() {
        return ResourcesRepository.findAll();
    }

    @Override
    public Resources getResources(UUID id) {
        return ResourcesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Resources Not Found"));
    }


    @Override
    public List<ResourceDTO> mapResourcesListDto(List<Resources> resources) {
        return resources.stream().map(this::mapResourceToDto).toList();
    }

    private ResourceDTO mapResourceToDto(Resources resources) {
        return ResourceDTO.builder()
                .id(resources.getId())
                .url(resources.getUrl())
                .name(resources.getName())
                .isPrimary(resources.getIsPrimary())
                .type(resources.getType())
                .build();
    }

    @Override
    public Resources convertResourcesDtoToEntity(ResourceDTO resource, Product product) {
        Resources resources = new Resources();
        if (null != resource.getId()) {
            resources.setId(resource.getId());
        }
        resources.setName(resource.getName());
        resources.setType(resource.getType());
        resources.setUrl(resource.getUrl());
        resources.setIsPrimary(resource.getIsPrimary());
        resources.setProduct(product);
        return resources;
    }


    @Override
    public List<Resources> convertResourcesDtoToEntities(List<ResourceDTO> resourcesList, Product product) {
        return resourcesList.stream().map(resourceDto -> convertResourcesDtoToEntity(resourceDto, product)).collect(Collectors.toList());
    }

    @Override
    public String getProductThumbnail(List<Resources> resources) {
        if (resources == null) {
            return null;
        }

        return resources.stream()
                .filter(Resources::getIsPrimary)
                .findFirst()
                .map(Resources::getUrl)
                .orElse(null);
    }
}
