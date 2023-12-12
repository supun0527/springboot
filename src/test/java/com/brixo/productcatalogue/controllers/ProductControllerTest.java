package com.brixo.productcatalogue.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.brixo.productcatalogue.Fixture;
import com.brixo.productcatalogue.dtos.ProductDto;
import com.brixo.productcatalogue.repositories.ServiceRepository;
import com.brixo.productcatalogue.repositories.ServiceTypeRepository;
import com.brixo.productcatalogue.services.ProductService;
import com.brixo.productcatalogue.services.ServiceService;
import com.brixo.productcatalogue.services.ServiceTypeService;
import com.brixo.restclient.RestClient;
import java.util.List;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private ProductService productService;

  @MockBean private ServiceService serviceService;

  @MockBean private ServiceTypeService serviceTypeService;

  @MockBean private ServiceRepository serviceRepository;

  @MockBean private ServiceTypeRepository serviceTypeRepository;

  @MockBean private RestClient restClient;

  @Test
  void testCreateProduct() throws Exception {

    // Given
    ProductDto productDto = Fixture.createProduct(Boolean.FALSE);

    when(productService.createProduct(any(ProductDto.class))).thenReturn(productDto);

    // Test
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/v1/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productDto)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(jsonPath("$.name").value(productDto.getName()))
        .andExpect(jsonPath("$.productKey").value(productDto.getProductKey()))
        .andExpect(jsonPath("$.updatedBy").value(productDto.getUpdatedBy()));

    // Verify
    verify(productService, times(1)).createProduct(productDto);
  }

  @Test
  void testUpdateProduct_Success() throws Exception {
    // Given
    ProductDto inputProductDto = Fixture.createProduct(Boolean.FALSE);
    inputProductDto.setId(1);

    ProductDto updatedProductDto = Fixture.createProduct(Boolean.FALSE);
    updatedProductDto.setId(1);
    updatedProductDto.setProductKey("Flexkanto");

    // when
    when(productService.updateProduct(eq(inputProductDto.getId()), eq(inputProductDto)))
        .thenReturn(updatedProductDto);

    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/v1/api/products/{id}", inputProductDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(inputProductDto)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(jsonPath("$.name").value(updatedProductDto.getName()))
        .andExpect(jsonPath("$.productKey").value(updatedProductDto.getProductKey()))
        .andExpect(jsonPath("$.updatedBy").value(updatedProductDto.getUpdatedBy()));

    // Verify
    verify(productService, times(1))
        .updateProduct(eq(inputProductDto.getId()), eq(inputProductDto));
  }

  @Test
  void testUpdateProduct_ProductNotFound() throws Exception {
    // Given
    ProductDto inputProductDto = Fixture.createProduct(Boolean.FALSE);
    inputProductDto.setId(1);

    when(productService.updateProduct(eq(inputProductDto.getId()), eq(inputProductDto)))
        .thenReturn(null);

    // Test
    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/v1/api/products/{id}", inputProductDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(inputProductDto)))
        .andExpect(MockMvcResultMatchers.status().isNotFound());

    // Verify
    verify(productService, times(1))
        .updateProduct(eq(inputProductDto.getId()), eq(inputProductDto));
  }

  @Test
  void testGetAllProducts() throws Exception {
    // Given
    List<ProductDto> productList = Fixture.createProducts(5);

    when(productService.getAllProducts(false)).thenReturn(productList);

    // When
    mockMvc
        .perform(get("/v1/api/products/all"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", Matchers.hasSize(5)))
        .andExpect(jsonPath("$[0].name").value(productList.get(0).getName()))
        .andExpect(jsonPath("$[0].productKey").value(productList.get(0).getProductKey()));
  }

  @Test
  void testGetAllProducts_Error() throws Exception {
    // Setup
    when(productService.getAllProducts(false))
        .thenThrow(new RuntimeException("Error retrieving products"));

    // Test
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/v1/api/products/all")
                .param("isDisabled", "false")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isInternalServerError());

    // Verify
    verify(productService, times(1)).getAllProducts(false);
  }

  @Test
  void testGetProductByIdOrProductKey_ProductById_Success() throws Exception {
    // Setup
    ProductDto inputProductDto = Fixture.createProduct(Boolean.FALSE);
    inputProductDto.setId(1);

    when(productService.getProductByIdOrProductKey(eq(inputProductDto.getId()), isNull()))
        .thenReturn(Optional.of(inputProductDto));

    // Test
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/v1/api/products")
                .param("id", String.valueOf(inputProductDto.getId()))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(jsonPath("$.name").value(inputProductDto.getName()))
        .andExpect(jsonPath("$.productKey").value(inputProductDto.getProductKey()))
        .andExpect(jsonPath("$.updatedBy").value(inputProductDto.getUpdatedBy()));

    // Verify
    verify(productService, times(1))
        .getProductByIdOrProductKey(eq(inputProductDto.getId()), isNull());
  }

  @Test
  void testGetProductByIdOrProductKey_ProductByProductKey_Success() throws Exception {
    // Given
    ProductDto inputProductDto = Fixture.createProduct(Boolean.FALSE);

    when(productService.getProductByIdOrProductKey(isNull(), eq(inputProductDto.getProductKey())))
        .thenReturn(Optional.of(inputProductDto));

    // Test
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/v1/api/products")
                .param("productKey", inputProductDto.getProductKey())
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(jsonPath("$.name").value(inputProductDto.getName()))
        .andExpect(jsonPath("$.productKey").value(inputProductDto.getProductKey()))
        .andExpect(jsonPath("$.updatedBy").value(inputProductDto.getUpdatedBy()));

    // Verify
    verify(productService, times(1))
        .getProductByIdOrProductKey(isNull(), eq(inputProductDto.getProductKey()));
  }

  @Test
  void testGetProductByIdOrProductKey_ProductNotFound() throws Exception {
    // Setup
    int productId = 1;
    String productKey = "abc123";

    when(productService.getProductByIdOrProductKey(eq(productId), isNull()))
        .thenReturn(Optional.empty());
    when(productService.getProductByIdOrProductKey(isNull(), eq(productKey)))
        .thenReturn(Optional.empty());

    // Test
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/v1/api/products")
                .param("id", String.valueOf(productId))
                .param("productKey", productKey)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  // Utility method to convert objects to JSON string
  private static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
