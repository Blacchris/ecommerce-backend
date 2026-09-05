package com.example.ecommerce.product.service;

import com.example.ecommerce.common.exception.InvalidRequestException;
import com.example.ecommerce.common.exception.ResourceNotFoundException;
import com.example.ecommerce.product.dto.RequestProductDTO;
import com.example.ecommerce.product.dto.ResponseProductDTO;
import com.example.ecommerce.product.dto.UpdateProductDTO;
import com.example.ecommerce.product.entity.Product;
import com.example.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepo;

    private final static String RESOURCE_NOT_FOUND = "Product with id: %s not found";

    public List<ResponseProductDTO> getProducts() {
        return productRepo.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ResponseProductDTO getProductById(Long id) throws ResourceNotFoundException {
        Product product = findOrThrow(id);
        return toResponse(product);
    }

    public ResponseProductDTO createProduct(RequestProductDTO req) {
        Product product = Product.builder()
                .name(req.getName().trim())
                .description(req.getDescription().trim())
                .price(req.getPrice())
                .stock(req.getStock())
                .build();

        Product saved = productRepo.save(product);
        return toResponse(product);
    }


    @Transactional
    public ResponseProductDTO updateProduct(UpdateProductDTO req, Long id) throws ResourceNotFoundException {

        Product found = findOrThrow(id);
        if (req.getName() != null && !req.getName().isBlank()) {
            found.changeName(req.getName());
        }
        if (req.getDescription() != null && !req.getDescription().isBlank()) {
            found.changeDescription(req.getDescription());
        }

        if (req.getPrice() != null) {
            found.changePrice(req.getPrice());
        }

        if (req.getStock() != null) {
            found.changeStock(req.getStock());
        }

        Product update = productRepo.save(found);
        return toResponse(update);
    }

    public void deleteProductById(Long id) throws ResourceNotFoundException {
        if (!productRepo.existsById(id)) {
            throw new ResourceNotFoundException(
                    String.format(RESOURCE_NOT_FOUND, id)
            );
        }
        productRepo.deleteById(id);
    }


    private void validate(BigDecimal price, Integer stock) {
        if (price != null && price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidRequestException("Price cannot be less than Zero");
        }
        if (stock != null && stock < 0) {
            throw new InvalidRequestException("Stock cannot be less than Zero");
        }
    }

    private Product findOrThrow(Long id) throws ResourceNotFoundException {
        return productRepo.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(String
                        .format(RESOURCE_NOT_FOUND, id)));
    }

    private ResponseProductDTO toResponse(Product product) {
        return ResponseProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

}
