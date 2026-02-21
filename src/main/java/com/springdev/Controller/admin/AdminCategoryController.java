package com.springdev.Controller.admin;


import com.springdev.DTO.CategoryRequest;
import com.springdev.DTO.CategoryResponse;
import com.springdev.Service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/admin/category")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

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
