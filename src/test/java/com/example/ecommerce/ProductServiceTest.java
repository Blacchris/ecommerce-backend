package com.example.ecommerce;

import com.example.ecommerce.common.exception.InvalidRequestException;
import com.example.ecommerce.common.exception.ResourceNotFoundException;
import com.example.ecommerce.product.dto.RequestProductDTO;
import com.example.ecommerce.product.dto.ResponseProductDTO;
import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.repository.ProductRepository;
import com.example.ecommerce.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepo;

    @InjectMocks
    private ProductService productService;

    @Test
    void getProductById() throws ResourceNotFoundException {
        Product product = Product
                .builder()
                .id(1L)
                .name("Samsung")
                .description("Best MobilePhone ever")
                .price(BigDecimal.valueOf(1999.99))
                .stock(100)
                .build();

        when(productRepo.findById(1L)).
                thenReturn(Optional.of(product));
        ResponseProductDTO result = productService.getProductById(1L);

        assertThat(result.getId()).isEqualTo(product.getId());
        assertThat(result.getName()).isEqualTo(product.getName());
        assertThat(result.getDescription()).isEqualTo(product.getDescription());
        assertThat(result.getPrice()).isEqualTo(product.getPrice());
        assertThat(result.getStock()).isEqualTo(product.getStock());
    }

    @Test
    void getProductById_mustThrowResourceNotFoundException() {

        when(productRepo.findById(999L)).
                thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");

    }

    @Test
    void createProduct_whenRequestIsValid_returnsSavedProductAsDto() {
        RequestProductDTO req = new RequestProductDTO();
        req.setName("Iphone");
        req.setDescription("Worst MobilePhone ever");
        req.setPrice(BigDecimal.valueOf(1999.99));
        req.setStock(100);

        Product savedproduct = Product
                .builder()
                .id(1L)
                .name("Iphone")
                .description("Worst MobilePhone ever")
                .price(BigDecimal.valueOf(1999.99))
                .stock(100)
                .build();

        when(productRepo.save(any(Product.class)))
                .thenReturn(savedproduct);

        ResponseProductDTO res = productService.createProduct(req);

        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);

        verify(productRepo).save(argumentCaptor.capture());

        Product capturedProduct = argumentCaptor.getValue();

        assertThat(capturedProduct.getName()).isEqualTo(req.getName());
        assertThat(capturedProduct.getDescription()).isEqualTo(req.getDescription());


    }


































}