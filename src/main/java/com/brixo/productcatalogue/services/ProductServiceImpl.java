package com.brixo.productcatalogue.services;

import com.brixo.productcatalogue.dtos.ProductDto;
import com.brixo.productcatalogue.models.Product;
import com.brixo.productcatalogue.repositories.ProductRepository;
import com.brixo.productcatalogue.repositories.ServiceTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final ServiceTypeRepository serviceTypeRepository;
  private final ModelMapper modelMapper;

  public ProductServiceImpl(
      ProductRepository productRepository,
      ServiceTypeRepository serviceTypeRepository,
      ModelMapper modelMapper) {
    this.productRepository = productRepository;
    this.serviceTypeRepository = serviceTypeRepository;
    this.modelMapper = modelMapper;
  }

  @Override
  public ProductDto createProduct(ProductDto productDto) {
    Product product = mapToEntity(productDto);
    Product savedProduct = productRepository.save(product);
    log.info("Product saved with ID: {}", savedProduct.getId());
    return mapToDto(savedProduct);
  }

  @Override
  public ProductDto updateProduct(int productId, ProductDto productDto) {
    Product existingProduct =
        productRepository
            .findById(productId)
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));

    existingProduct.setName(productDto.getName());
    existingProduct.setDisabled(productDto.isDisabled());
    existingProduct.setUpdatedBy(productDto.getUpdatedBy());
    modelMapper.map(productDto, existingProduct);
    Product updatedProduct = productRepository.save(existingProduct);
    return mapToDto(updatedProduct);
  }

  @Override
  public Optional<ProductDto> getProductByIdOrProductKey(Integer id, String productKey) {
    if (id != null) {
      Optional<Product> productById = productRepository.findById(id);
      return productById.map(this::mapToDto);
    } else if (productKey != null) {
      Optional<Product> productByProductKey = productRepository.findByProductKey(productKey);
      return productByProductKey.map(this::mapToDto);
    } else {
      return Optional.empty();
    }
  }

  @Override
  public List<ProductDto> getAllProducts(boolean isDisabled) {
    List<Product> products;
    if (isDisabled) {
      products = productRepository.findAll();
    } else {
      products = productRepository.findByIsDisabledFalse();
    }
    return products.stream().map(this::mapToDto).toList();
  }

  private ProductDto mapToDto(Product product) {
    return modelMapper.map(product, ProductDto.class);
  }

  private Product mapToEntity(ProductDto productDto) {
    return modelMapper.map(productDto, Product.class);
  }
}
