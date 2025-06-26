package com.ecommerce.project.service;

import com.ecommerce.project.dto.CategoryDTO;
import com.ecommerce.project.dto.CategoryResponse;
import com.ecommerce.project.exceptionHandler.APIException;
import com.ecommerce.project.exceptionHandler.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryResponse getCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        List<CategoryDTO> dtos = categoryPage.getContent().stream().map(category ->
                modelMapper.map(category,CategoryDTO.class)).toList();

        CategoryResponse response = new CategoryResponse();
        response.setContent(dtos);
        response.setPageNumber(categoryPage.getNumber());
        response.setPageSize(categoryPage.getSize());
        response.setTotalPages(categoryPage.getTotalPages());
        response.setLastPage(categoryPage.isLast());
        response.setTotalElements(categoryPage.getTotalElements());

        return response;
    }

    @Override
    @Transactional
    public CategoryDTO createCategory(Category category) {
        Category returnedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(returnedCategory != null)
            throw new APIException(category.getCategoryName() + " already exist");
        Category category1 = categoryRepository.save(category);

        return modelMapper.map(category1,CategoryDTO.class);
    }

    @Override
    @Transactional
    public String deleteCategory(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()){
            throw new ResourceNotFoundException("Specified category id does not exist!");
        }

        categoryRepository.delete(category.get());
        return "Category of id : "+id +"is successfully deleted";

    }

    @Override
    @Transactional
    public String updateCategory(CategoryDTO categoryDTO, long id) {
        Optional<Category> category1 = categoryRepository.findById(id);
        if(category1.isEmpty()){
            throw new ResourceNotFoundException("Specified category id does not exist!");
        }


        Category category = modelMapper.map(categoryDTO,Category.class);
        category.setId(id);
        categoryRepository.save(category);
        return "Category successfully updated";


    }
}
