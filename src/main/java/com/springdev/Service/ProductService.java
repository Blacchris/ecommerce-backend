package com.springdev.Service;

import com.springdev.CustomExceptions.ProductNotFoundException;
import com.springdev.DTO.AddProduct;
import com.springdev.DTO.ProductResponse;
import com.springdev.DTO.ProductUpdateDTO;
import com.springdev.Entity.Product;
import com.springdev.Entity.User;
import com.springdev.Repository.ProductRepository;
import com.springdev.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
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
    @Transactional /*If something fails, undo everything that happened inside this method.
    A transaction is a group of database operations that must succeed or fail together.
    */
    public List<ProductResponse> createProduct(List<AddProduct> addProduct){
//        User user = userRepository.findById(adminId)
//                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, adminId)));

        List<Product> products = addProduct.stream()
                .map(p -> Product.builder()
                                .name(p.getName())
                                .description(p.getDescription())
                                .price((p.getPrice()))
                                .stock(p.getStock())
                                .build()).toList();

        List<Product> saveProducts = productRepository.saveAll(products);
        return saveProducts.stream()
                .map(this::fetchProducts)
                .toList();
    }

    public ProductResponse updateProduct(long id, ProductUpdateDTO productUpdateDTO){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND, id)));
        if(productUpdateDTO.getName() != null) {
            product.setName(productUpdateDTO.getName());
        }

        if(productUpdateDTO.getDescription() != null){
            product.setDescription(productUpdateDTO.getDescription());
        }

        if(productUpdateDTO.getPrice() != null){
            product.setPrice(productUpdateDTO.getPrice());
        }

        if(productUpdateDTO.getStock() != 0){
            product.setStock(productUpdateDTO.getStock());
        }

        return fetchProducts(product);
    }


    public void deleteProductById(long id){
        if(productRepository.findById(id).isEmpty()){
            throw new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND, id));
        }
        productRepository.deleteById(id);
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
