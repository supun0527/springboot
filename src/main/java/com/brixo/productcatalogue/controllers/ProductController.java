package com.brixo.productcatalogue.controllers;

import com.brixo.productcatalogue.dtos.ProductDto;
import com.brixo.productcatalogue.services.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/products")
@Slf4j
@Validated
public class ProductController {

  private final ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping
  public ResponseEntity<ProductDto> createProduct(@RequestBody @Valid ProductDto productDto) {
    try {
      ProductDto createdProduct = productService.createProduct(productDto);
      return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    } catch (Exception ex) {
      log.error("Error creating product: {}", ex.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductDto> updateProduct(
      @PathVariable int id, @RequestBody @Valid ProductDto productDto) {
    try {
      ProductDto updatedProduct = productService.updateProduct(id, productDto);
      if (updatedProduct != null) {
        return ResponseEntity.ok(updatedProduct);
      } else {
        return ResponseEntity.notFound().build();
      }
    } catch (Exception ex) {
      log.error("Error updating product: {}", ex.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @GetMapping("/all")
  public ResponseEntity<List<ProductDto>> getAllProducts(
      @RequestParam(name = "isDisabled", defaultValue = "false") boolean isDisabled) {
    try {
      List<ProductDto> products = productService.getAllProducts(isDisabled);
      return ResponseEntity.ok(products);
    } catch (Exception ex) {
      log.error("Error listing products: {}", ex.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @GetMapping
  public ResponseEntity<ProductDto> getProductByIdOrProductKey(
      @RequestParam(required = false) Integer id,
      @RequestParam(required = false) String productKey) {

    if (id != null) {
      Optional<ProductDto> productById = productService.getProductByIdOrProductKey(id, null);
      return productById.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    } else if (productKey != null) {
      Optional<ProductDto> productByProductKey =
          productService.getProductByIdOrProductKey(null, productKey);
      return productByProductKey
          .map(ResponseEntity::ok)
          .orElseGet(() -> ResponseEntity.notFound().build());
    } else {
      return ResponseEntity.badRequest().build();
    }
  }
}
