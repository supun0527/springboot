package com.brixo.productcatalogue.services;

import com.brixo.productcatalogue.Fixture;
import com.brixo.productcatalogue.IntegrationTestSuperclass;
import com.brixo.productcatalogue.dtos.ProductDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class ProductServiceIntegrationTest extends IntegrationTestSuperclass {
  @Autowired private ProductService productService;

  @Test
  void testCreateProduct() {
    // Given
    ProductDto inputProductDto = Fixture.createProduct(Boolean.FALSE);
    // When
    ProductDto createdProductDto = productService.createProduct(inputProductDto);
    // Then
    assertTrue(createdProductDto.getId() > 0);
  }

  @Test
  void testUpdateProduct() {
    // Given
    ProductDto inputProductDto = Fixture.createProduct(Boolean.FALSE);
    ProductDto createdProductDto = productService.createProduct(inputProductDto);

    // When
    inputProductDto.setName("Mirax Credit");
    ProductDto updatedProductDto =
        productService.updateProduct(createdProductDto.getId(), inputProductDto);
    // Then
    assertEquals(inputProductDto.getName(), updatedProductDto.getName());
  }

  @Test
  void testGetProductByIdOrProductKey() {
    // Setup
    ProductDto inputProductDtoOne = Fixture.createProduct(Boolean.FALSE);
    ProductDto inputProductDtoTwo = Fixture.createProduct(Boolean.FALSE);
    ProductDto createdProductDtoOne = productService.createProduct(inputProductDtoOne);
    ProductDto createdProductDtoTwo = productService.createProduct(inputProductDtoTwo);

    // Test
    List<ProductDto> productDtos = productService.getAllProducts(false);
    assertEquals(2, productDtos.size());

    // Verify
    assertEquals(createdProductDtoOne.getId(), productDtos.get(0).getId());
    assertEquals(createdProductDtoTwo.getId(), productDtos.get(1).getId());
  }

  @Test
  void testGetProductByIdOrProductKey_ProductById_Success() {
    // Given
    ProductDto inputProductDto = Fixture.createProduct(Boolean.FALSE);
    ProductDto createdProductDto = productService.createProduct(inputProductDto);

    // Act
    Optional<ProductDto> result =
        productService.getProductByIdOrProductKey(createdProductDto.getId(), null);

    // Assert
    assertTrue(result.isPresent());
    assertEquals(createdProductDto.getId(), result.get().getId());
  }

  @Test
  void testGetProductByIdOrProductKey_ProductByProductKey_Success() {
    // Arrange
    ProductDto inputProductDto = Fixture.createProduct(Boolean.FALSE);
    ProductDto createdProductDto = productService.createProduct(inputProductDto);

    // Act
    Optional<ProductDto> result =
        productService.getProductByIdOrProductKey(null, createdProductDto.getProductKey());

    // Assert
    assertTrue(result.isPresent());
    assertEquals(createdProductDto.getId(), result.get().getId());
  }

  @Test
  void testGetProductByIdOrProductKey_ProductNotFound() {
    // Arrange
    int nonExistingId = 999; // Assuming this ID does not exist
    String nonExistingProductKey = "nonExistingKey"; // Assuming this product key does not exist

    // Act
    Optional<ProductDto> resultById =
        productService.getProductByIdOrProductKey(nonExistingId, null);
    Optional<ProductDto> resultByProductKey =
        productService.getProductByIdOrProductKey(null, nonExistingProductKey);

    // Assert
    assertFalse(resultById.isPresent());
    assertFalse(resultByProductKey.isPresent());
  }

  @Test
  void testGetAllProducts() {

    ProductDto inputProductDtoOne = Fixture.createProduct(Boolean.FALSE);
    ProductDto inputProductDtoTwo = Fixture.createProduct(Boolean.FALSE);
    ProductDto createdProductDtoOne = productService.createProduct(inputProductDtoOne);
    ProductDto createdProductDtoTwo = productService.createProduct(inputProductDtoTwo);

    // Test
    List<ProductDto> productDtos = productService.getAllProducts(false);
    assertEquals(2, productDtos.size());

    // Verify
    assertEquals(createdProductDtoOne.getId(), productDtos.get(0).getId());
    assertEquals(createdProductDtoTwo.getId(), productDtos.get(1).getId());
  }
}
