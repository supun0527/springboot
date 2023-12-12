package com.brixo.productcatalogue.services;

import com.brixo.productcatalogue.dtos.ProductDto;

import java.util.List;
import java.util.Optional;

public interface ProductService {

  ProductDto createProduct(ProductDto productDto);

  ProductDto updateProduct(int productId, ProductDto productDto);

  Optional<ProductDto> getProductByIdOrProductKey(Integer id, String productKey);

  List<ProductDto> getAllProducts(boolean isDisabled);
}
