package com.brixo.productcatalogue.controllers;

import com.brixo.exceptionmanagement.exceptions.InvalidMethodInputException;
import com.brixo.productcatalogue.dtos.ProductDto;
import com.brixo.productcatalogue.services.ProductService;
import jakarta.validation.Valid;

import java.util.List;

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
        ProductDto createdProduct = productService.createProduct(productDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable int id, @RequestBody @Valid ProductDto productDto) {
        ProductDto updatedProduct = productService.updateProduct(id, productDto);
        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDto>> getAllProducts(
            @RequestParam(name = "isDisabled", defaultValue = "false") boolean isDisabled) {
        List<ProductDto> products = productService.getAllProducts(isDisabled);
        return ResponseEntity.ok(products);
    }

    @GetMapping
    public ResponseEntity<ProductDto> getProductByIdOrProductKey(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String productKey) {
        if (id == null && productKey == null) {
            throw new InvalidMethodInputException("Either id or productKey should be provided.");
        }
        return new ResponseEntity<>(id != null ? productService.getProductById(id) : productService.getProductByKey(productKey), HttpStatus.OK);
    }

}
