package com.springdev.Controller;

import com.springdev.DTO.AddProduct;
import com.springdev.DTO.ProductResponse;
import com.springdev.DTO.ProductUpdateDTO;
import com.springdev.Entity.CustomUserDetails;
import com.springdev.Entity.Product;
import com.springdev.Service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping(path = "/api/v1/product")
@Slf4j
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<ProductResponse> getProduct(
            @PathVariable long id){
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @GetMapping(path = "/products")
    public ResponseEntity<List<ProductResponse>> getProducts(){
        return ResponseEntity.ok(productService.getProducts());
    }

    @PostMapping(path= "/products")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductResponse>> createProduct(
            @RequestBody List<AddProduct> addProduct
            ) {
        return ResponseEntity.status(201).body(productService.createProduct(addProduct));
    }

    @PatchMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable long id,
            @RequestBody ProductUpdateDTO productUpdateDTO
    ) {
        return ResponseEntity.ok(productService.updateProduct(id, productUpdateDTO));
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProductById(@PathVariable long id){
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }


}
