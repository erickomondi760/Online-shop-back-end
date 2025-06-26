package com.ecommerce.project.service;

import com.ecommerce.project.dto.CategoryDTO;
import com.ecommerce.project.dto.CategoryResponse;
import com.ecommerce.project.model.Category;


public interface CategoryService {

    CategoryResponse getCategories(Integer pageNumber,Integer pageSize,String sortBy, String sortOrder);
    CategoryDTO createCategory(Category category);
    String deleteCategory(Long id);

    String updateCategory(CategoryDTO category, long id);
}
