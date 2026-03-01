//package com.springdev.config;
//
//import com.github.javafaker.Faker;
//import com.springdev.Entity.Category;
//import com.springdev.Entity.Product;
//import com.springdev.Entity.User;
//import com.springdev.Repository.CategoryRepository;
//import com.springdev.Repository.ProductRepository;
//import com.springdev.Repository.UserRepository;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//import java.util.stream.IntStream;
//
//@Configuration
//@Profile("dev")   // 🔥 Only runs in dev
//@RequiredArgsConstructor
//public class DevDataLoader implements CommandLineRunner {
//
//    private final ProductRepository productRepository;
//    private final CategoryRepository categoryRepository;
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    @Transactional
//    public void run(String... args) {
//
//        if (productRepository.count() > 0) return;
//
//        Faker faker = new Faker();
//        Random random = new Random();
//
//        // Create categories
//        Category electronics = new Category("Electronics", faker.lorem().sentence());
//        Category clothing = new Category("Clothing", faker.lorem().sentence());
//        Category books = new Category("Books", faker.lorem().sentence());
//
//        categoryRepository.saveAll(List.of(electronics, clothing, books));
//
//        List<Category> categories = List.of(electronics, clothing, books);
//        List<Product> products = new ArrayList<>();
//
//        List<User> users = new ArrayList<>();
//        for(int i = 0; i < 200; ++i){
//            User user = new User();
//            user.setUsername(faker.name().username());
//            user.setEmail(faker.internet().emailAddress());
//            user.setPassword(passwordEncoder.encode(faker.internet().password()));
//            users.add(user);
//        }
//        userRepository.saveAll(users);
//        // Generate 1000 products
//        IntStream.range(0, 1000).forEach(i -> {
//
//            Product product = new Product();
//
//            product.setName(faker.commerce().productName());
//            product.setDescription(faker.lorem().sentence());
//            product.setPrice(new BigDecimal(faker.commerce().price().replace(", ", "")));
//            product.setStock(random.nextInt(200));
//            product.setSeller(users.get(random.nextInt(users.size())));
//            product.setCategory(categories.get(random.nextInt(categories.size())));
//            products.add(product);
//        });
//
//        productRepository.saveAll(products);
//
//        System.out.println("🔥 1000 Fake Products Loaded Successfully!");
//    }
//
//
//}