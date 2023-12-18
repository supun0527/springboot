package com.brixo.productcatalogue.services;

import com.brixo.productcatalogue.dtos.ProductDto;
import com.brixo.productcatalogue.models.Product;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public interface ProductService {

  ProductDto createProduct(ProductDto productDto);

  ProductDto updateProduct(int productId, ProductDto productDto);

  List<ProductDto> getAllProducts(boolean isDisabled);

  ProductDto getProductById(Integer id);

  Product findById(Integer id);

  ProductDto getProductByKey(String key);

  Product findByKey(String key);
}
