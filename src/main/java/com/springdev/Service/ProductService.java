package com.springdev.Service;

import com.springdev.CustomExceptions.ProductNotFoundException;
import com.springdev.DTO.AddProduct;
import com.springdev.DTO.ProductResponse;
import com.springdev.Entity.Product;
import com.springdev.Entity.User;
import com.springdev.Repository.ProductRepository;
import com.springdev.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {


    private final String PRODUCT_NOT_FOUND = "Product not found with id: %d";
    private final String USER_NOT_FOUND = "User not found with id: %d";

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public List<ProductResponse> getProducts(){
        return productRepository.findAll()
                .stream()
                .map(this::fetchProducts)
                .toList();
    }

    public ProductResponse getProduct(long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND, id)));
        return fetchProducts(product);
    }

    //Admin
    public ProductResponse createProduct(long adminId, AddProduct addProduct){
        User user = userRepository.findById(adminId)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, adminId)));

        return fetchProducts(
                productRepository.save(
                        Product.builder()
                                .name(addProduct.getName())
                                .description(addProduct.getDescription())
                                .price(addProduct.getPrice())
                                .stock(addProduct.getStock())
                                .build()
                )
        );
    }

    private ProductResponse fetchProducts(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }


}
