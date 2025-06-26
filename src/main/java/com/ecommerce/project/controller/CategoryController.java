package com.ecommerce.project.controller;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.configurations.AppConstants;
import com.ecommerce.project.dto.CategoryDTO;
import com.ecommerce.project.dto.CategoryResponse;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/api/public/categories")
    public ResponseEntity<CategoryResponse> getCategories(
           @RequestParam(name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
           @RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
           @RequestParam(name="sortBy",defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
           @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_ORDER,required = false) String sortOrder){
        return new ResponseEntity<>(categoryService.getCategories(pageNumber,pageSize,sortBy,sortOrder),HttpStatus.OK);
    }


    @PostMapping("/api/public/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody Category category){
        CategoryDTO categoryDTO1 =categoryService.createCategory(category);
        return new ResponseEntity<>(categoryDTO1,HttpStatus.CREATED);
    }

    @DeleteMapping("/api/admin/categories/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable long id){

        try{
            return new ResponseEntity<>(categoryService.deleteCategory(id),HttpStatus.OK);
        }catch (ResponseStatusException e){
            return new ResponseEntity<>(e.getReason(),e.getStatusCode());
        }
    }

    @PutMapping("/api/admin/categories/{id}")
    public ResponseEntity<String> updateCategory(@RequestBody CategoryDTO categoryDTO,@PathVariable long id){
        try {
            String status = categoryService.updateCategory(categoryDTO, id);
            return ResponseEntity.ok(status);
        }catch (ResponseStatusException e){
            return new ResponseEntity<>("Category does not exist",HttpStatus.NOT_FOUND);
        }
    }


}
