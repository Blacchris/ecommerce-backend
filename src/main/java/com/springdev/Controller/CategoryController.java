package com.springdev.Controller;


import com.springdev.DTO.CategoryRequest;
import com.springdev.DTO.CategoryResponse;
import com.springdev.Service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/api/categories")
@RequiredArgsConstructor
public class CategoryController {


    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories(){
        return ResponseEntity.ok(categoryService.getCategories());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest categoryRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.createCategory(categoryRequest));
    }

//    @PutMapping(path = "/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<CategoryResponse> updateCategory(
//            @Valid @RequestBody CategoryRequest categoryRequest
//    ){
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(categoryService.)
//    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}
