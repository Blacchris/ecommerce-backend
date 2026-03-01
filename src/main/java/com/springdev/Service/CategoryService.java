package com.springdev.Service;


import com.springdev.CustomExceptions.CategoryAlreadyExistException;
import com.springdev.CustomExceptions.CategoryNotFoundException;
import com.springdev.DTO.CategoryRequest;
import com.springdev.DTO.CategoryResponse;
import com.springdev.Entity.Category;
import com.springdev.Repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryResponse> getCategories(){
        return categoryRepository.findAll()
                .stream()
                .map(this::fetchCategory)
                .toList();
    }

//    public CategoryResponse getCategoryById(long id){
//        Category category = categoryRepository.findById(id)
//                .orElseThrow(() -> new CategoryNotFoundException("Category with id: " + id + " not found"));
//        return fetchCategory(category);
//    }

    public CategoryResponse createCategory(CategoryRequest categoryRequest){
        if(categoryRepository.existsByName(categoryRequest.getName())){
            throw new CategoryAlreadyExistException("Category name already exists");
        }

        return fetchCategory(
                categoryRepository.save(
                        new Category(
                                categoryRequest.getName(),
                                categoryRequest.getDescription()
                        )
                )
        );
    }

//    public CategoryResponse updateCategory(long id, CategoryRequest categoryRequest){
//        Category category = categoryRepository.findById(id)
//                .orElseThrow(() -> new CategoryNotFoundException(String.format("Category with id: %s is not found", id)));
//
//        if(categoryRepository.existsByName(categoryRequest.getName())){
//            throw new CategoryAlreadyExistException("Category name already exists");
//        }
//
//
//        category.setName(categoryRequest.getName());
//        return fetchCategory(categoryRepository.save(category));
//    }


    public void deleteCategory(long id){
        Category category = categoryRepository.findById(id)
                        .orElseThrow(() -> new CategoryNotFoundException(String.format("Category with id: %s is not found", id)));;
        categoryRepository.delete(category);
    }

    private CategoryResponse fetchCategory(Category category){
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

}
