package com.springdev.Service;

import com.springdev.CustomExceptions.CategoryNotFoundException;
import com.springdev.CustomExceptions.ProductNotFoundException;
import com.springdev.CustomExceptions.SellerNotFoundException;
import com.springdev.DTO.AddProduct;
import com.springdev.DTO.ProductResponse;
import com.springdev.DTO.ProductUpdateDTO;
import com.springdev.Entity.Product;
import com.springdev.Entity.Seller;
import com.springdev.Repository.CategoryRepository;
import com.springdev.Repository.ProductRepository;
import com.springdev.Repository.SellerRepository;
import com.springdev.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {


    private final String PRODUCT_NOT_FOUND = "Product not found with id: %d";
    private final String SELLER_NOT_FOUND = "Seller not found with id: %s";
    private final String USER_NOT_FOUND = "User not found with id: %d";

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final SellerRepository sellerRepository;

    public List<ProductResponse> getAllProductsBySeller(long userId){
        Seller seller = sellerRepository.findByUserId(userId)
                .orElseThrow(() -> new SellerNotFoundException("You are not an approved seller"));

        return seller.getProductList()
                .stream()
                .map(this::fetchProducts)
                .toList();
    }

    public List<ProductResponse> getAllProducts(){
        return productRepository.findAll()
                .stream()
                .map(this::fetchProducts)
                .toList();
    }

    public ProductResponse getProduct(long sellerId, long productId){
        Seller seller = sellerRepository.findByUserId(sellerId)
                .orElseThrow(() -> new SellerNotFoundException("Seller not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND, productId)));
        if(product.getSeller() != seller){
            throw new IllegalStateException("Product mismatch");
        }
        return fetchProducts(product);
    }



    //Admin
    @Transactional /*If something fails, undo everything that happened inside this method.
    A transaction is a group of database operations that must succeed or fail together.
    */
    public List<ProductResponse> createProduct(long userId, List<AddProduct> addProduct){

        Seller seller = sellerRepository.findByUserId(userId)
                .orElseThrow(() -> new SellerNotFoundException(String.format(SELLER_NOT_FOUND, userId)));
        List<Product> products = addProduct.stream()
                .map(p -> Product.builder()
                                .name(p.getName())
                                .description(p.getDescription())
                                .price((p.getPrice()))
                                .stock(p.getStock())
                        .category(categoryRepository.findById(p.getCategory_id())
                                .orElseThrow(() -> new CategoryNotFoundException("category not found")))
                        .seller(seller)
                                .build()).toList();

        List<Product> saveProducts = productRepository.saveAll(products);
        return saveProducts.stream()
                .map(this::fetchProducts)
                .toList();
    }

    public ProductResponse updateProduct(long userId, long productId, ProductUpdateDTO productUpdateDTO){

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if(product.getSeller().getUser().getId() != userId) {
            throw new IllegalStateException("You cannot update this product");
        }
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

//    public ProductResponse getSellerProduct(long Id, long sellerId){
//
//    }

//    public List<ProductResponse> getAllSellerProducts(long sellerId){
//        List<Product> products = productRepository.findAllByUser(sellerId)
//                .orElseThrow(() -> new ProductNotFoundException("Products not found"));
//        return products.stream()
//                .map(this::fetchProducts)
//                .toList();
//    }


    public void deleteProductById(long userId, long Id){
        Product product = productRepository.findById(Id)
                        .orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND, Id)));

        if(product.getSeller().getUser().getId() != userId){
            throw new IllegalStateException("You cannot delete this product");
        }
        productRepository.deleteById(Id);
    }

    private ProductResponse fetchProducts(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }


}
