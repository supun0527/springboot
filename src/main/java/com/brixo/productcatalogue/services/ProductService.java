package com.brixo.productcatalogue.services;

import com.brixo.productcatalogue.dtos.ProductDto;
import com.brixo.productcatalogue.models.Product;

import java.util.List;

public interface ProductService {

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(int productId, ProductDto productDto);

    List<ProductDto> getAllProducts(boolean isDisabled);

    ProductDto getProductById(Integer id);

    Product findById(Integer id);

    ProductDto getProductByKey(String key);

    Product findByKey(String key);
}
