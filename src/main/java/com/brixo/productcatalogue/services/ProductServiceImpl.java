package com.brixo.productcatalogue.services;

import com.brixo.productcatalogue.dtos.ProductDto;
import com.brixo.productcatalogue.mappers.AbstractMapper;
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
  private final AbstractMapper abstractMapper;

  public ProductServiceImpl(
          ProductRepository productRepository,
          ServiceTypeRepository serviceTypeRepository, AbstractMapper abstractMapper) {
    this.productRepository = productRepository;
    this.serviceTypeRepository = serviceTypeRepository;
    this.abstractMapper = abstractMapper;
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
    Product updatedProduct = productRepository.save(existingProduct);
    return mapToDto(updatedProduct);
  }

  @Override
  public ProductDto getProductById(Integer id){
    return abstractMapper.map(findById(id), ProductDto.class);
  }

  @Override
  public Product findById(Integer id){
   return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Could not find the product by id:" + id));
  }

  @Override
  public ProductDto getProductByKey(String key){
    return abstractMapper.map(findByKey(key), ProductDto.class);
  }

  @Override
  public Product findByKey(String key){
    return productRepository.findByProductKey(key).orElseThrow(() -> new EntityNotFoundException("Could not find the product by key:" + key));
  }

  @Override
  public List<ProductDto> getAllProducts(boolean isDisabled) {
    List<Product> products = isDisabled ? productRepository.findAll() : productRepository.findByIsDisabledFalse();
    return  products.stream().map(this::mapToDto).toList();
  }

  private ProductDto mapToDto(Product product) {
    return abstractMapper.map(product, ProductDto.class);
  }

  private Product mapToEntity(ProductDto productDto) {
    return abstractMapper.map(productDto, Product.class);
  }
}
