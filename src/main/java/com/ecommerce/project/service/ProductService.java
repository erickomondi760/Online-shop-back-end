package com.ecommerce.project.service;

import com.ecommerce.project.dto.ProductDTO;
import com.ecommerce.project.dto.ProductResponse;
import com.ecommerce.project.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO saveProduct(Product product, Long categoryId);

    ProductResponse getAllProducts(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);

    ProductDTO updateProduct(Product product, Long productId);

    void deleteProduct(Long productId);

    ProductDTO uploadImage(Long productId, MultipartFile image) throws IOException;

    ProductResponse getProductsByCategory(Long id, Integer pageNumber, Integer pageSize,
                                          String sortBy, String sortOrder);

    ProductResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize,
                                         String sortBy, String sortOrder);
}
