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

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping(path = "/{Id}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductResponse> getProduct(
            @PathVariable long Id,
            @AuthenticationPrincipal CustomUserDetails principal){
        long userId = principal.getUser().getId();
        return ResponseEntity.ok(productService.getProduct(userId, Id));
    }

    @GetMapping(path = "/products")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<List<ProductResponse>> getAllProductsBySeller(
            @AuthenticationPrincipal CustomUserDetails principal
    ){
        long userId = principal.getUser().getId();
        return ResponseEntity.ok(productService.getAllProductsBySeller(userId));
    }

    @PostMapping(path= "/products")
    @PreAuthorize("hasAnyRole('ADMIN','SELLER')")
    public ResponseEntity<List<ProductResponse>> createProduct(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestBody List<AddProduct> addProduct
            ) {
        long userId = principal.getUser().getId();
        return ResponseEntity.status(201).body(productService.createProduct(userId, addProduct));
    }

    @PatchMapping(path = "/{Id}")
    @PreAuthorize("hasRole('ADMIN', 'SELLER')")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable long Id,
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestBody ProductUpdateDTO productUpdateDTO
    ) {
        long userId = principal.getUser().getId();
        return ResponseEntity.ok(productService.updateProduct(userId, Id, productUpdateDTO));
    }

    @DeleteMapping(path = "/{Id}")
    @PreAuthorize("hasRole('ADMIN', 'SELLER')")
    public ResponseEntity<Void> deleteProductById(
            @PathVariable long Id,
            @AuthenticationPrincipal CustomUserDetails principal){
        long userId = principal.getUser().getId();
        productService.deleteProductById(userId, Id);
        return ResponseEntity.noContent().build();
    }


}
