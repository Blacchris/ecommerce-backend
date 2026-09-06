package com.example.ecommerce.product.controller;


import com.example.ecommerce.common.exception.ResourceNotFoundException;
import com.example.ecommerce.product.dto.RequestProductDTO;
import com.example.ecommerce.product.dto.ResponseProductDTO;
import com.example.ecommerce.product.dto.UpdateProductDTO;
import com.example.ecommerce.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ResponseProductDTO>> getProducts() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getProducts());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ResponseProductDTO> getProduct(
            @PathVariable Long id
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<ResponseProductDTO> createProduct(
            @Valid @RequestBody RequestProductDTO req
            ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(req));
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<ResponseProductDTO> updateProduct(
            @Valid @RequestBody UpdateProductDTO req,
            @PathVariable Long id
            ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.updateProduct(req, id));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id
    ) {
        productService.deleteProductById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
