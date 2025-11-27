package com.fleet.admin.services;

import com.fleet.admin.requests.CreateProductRequest;
import com.fleet.admin.requests.UpdateProductRequest;
import com.fleet.common.dto.ProductDto;
import com.fleet.common.helpers.ApiResponse;

import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface ProductService {

    ResponseEntity<ApiResponse<ProductDto>> createProduct(CreateProductRequest request);

    ResponseEntity<ApiResponse<ProductDto>> updateProduct(UpdateProductRequest request);

    ResponseEntity<ApiResponse<ProductDto>> getProduct(UUID id);
}
