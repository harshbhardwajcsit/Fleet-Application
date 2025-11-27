package com.fleet.common.mapper;


import java.time.Instant;
import java.util.Date;

import com.fleet.admin.requests.CreateProductRequest;
import com.fleet.common.dto.ProductDto;
import com.fleet.common.entity.Product;

public class ProductMapper {

    public static ProductDto toDto(Product product) {
        if (product == null) return null;

        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());

        return dto;
    }

    public static Product toEntity(CreateProductRequest dto) {
        if (dto == null) return null;

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setCreatedAt(Instant.now());
        product.setUpdatedAt(Instant.now());

        return product;
    }

    public static void updateEntity(Product product, com.fleet.admin.requests.UpdateProductRequest dto) {
        if (dto.getName() != null) {
            product.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            product.setDescription(dto.getDescription());
        }
        product.setUpdatedAt(Instant.now());
    }
}
