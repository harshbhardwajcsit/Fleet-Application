package com.fleet.admin.services.implementation;


import com.fleet.admin.requests.CreateProductRequest;
import com.fleet.admin.requests.UpdateProductRequest;
import com.fleet.admin.services.ProductService;
import com.fleet.common.constants.ProductConstants;
import com.fleet.common.dto.ProductDto;
import com.fleet.common.entity.Product;
import com.fleet.common.exceptions.ProductNotFoundException;
import com.fleet.common.helpers.ApiResponse;
import com.fleet.common.helpers.ApiResponseBuilder;
import com.fleet.common.mapper.ProductMapper;
import com.fleet.common.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ApiResponseBuilder responseBuilder;

    @Override
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(CreateProductRequest request) {

        Product product = ProductMapper.toEntity(request);
        productRepository.save(product);

        return responseBuilder.success(
                HttpStatus.CREATED,
                ProductConstants.PRODUCT_CREATE_SUCCESS,
                ProductMapper.toDto(product)
        );
    }

    @Override
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(UpdateProductRequest request) {

        Product product = productRepository.findById(request.getId())
                .orElseThrow(() -> new ProductNotFoundException(ProductConstants.PRODUCT_NOT_FOUND));

        ProductMapper.updateEntity(product, request);
        productRepository.save(product);

        return responseBuilder.success(
                HttpStatus.OK,
                ProductConstants.PRODUCT_UPDATE_SUCCESS,
                ProductMapper.toDto(product)
        );
    }

    @Override
    public ResponseEntity<ApiResponse<ProductDto>> getProduct(UUID id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(ProductConstants.PRODUCT_NOT_FOUND));

        return responseBuilder.success(
                HttpStatus.OK,
                ProductConstants.PRODUCT_FETCH_SUCCESS,
                ProductMapper.toDto(product)
        );
    }
}

