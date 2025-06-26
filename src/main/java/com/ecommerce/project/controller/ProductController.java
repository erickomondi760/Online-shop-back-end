package com.ecommerce.project.controller;

import com.ecommerce.project.configurations.AppConstants;
import com.ecommerce.project.dto.ProductDTO;
import com.ecommerce.project.dto.ProductResponse;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/admin/categories/{categoryId}/products")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody Product product, @PathVariable Long categoryId){
        ProductDTO savedProduct = productService.saveProduct(product,categoryId);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getProducts(
    @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)Integer pageNumber,
    @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE,required = false)Integer pageSize,
    @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_BY,required = false)String sortBy,
    @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_ORDER,required = false)String sortOrder){

        return new ResponseEntity<>(productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder),HttpStatus.OK);
    }

    @GetMapping("/public/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId,
    @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)Integer pageNumber,
    @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE,required = false)Integer pageSize,
    @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_BY,required = false)String sortBy,
    @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_ORDER,required = false)String sortOrder){
        return new ResponseEntity<>(productService.getProductsByCategory(categoryId,pageNumber,pageSize,sortBy,sortOrder),HttpStatus.OK);
    }


    @GetMapping("/public/products/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword,
    @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false)Integer pageNumber,
    @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE,required = false)Integer pageSize,
    @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_BY,required = false)String sortBy,
    @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_ORDER,required = false)String sortOrder){
        return new ResponseEntity<>(productService.getProductsByKeyword(keyword,pageNumber,pageSize,sortBy,sortOrder),HttpStatus.OK);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody Product product,@PathVariable Long productId){
        ProductDTO updatedProduct = productService.updateProduct(product,productId);
        return new ResponseEntity<>(updatedProduct,HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId){
        productService.deleteProduct(productId);
        return new ResponseEntity<>("Product successfully delete",HttpStatus.OK);
    }

    @PutMapping("/public/products/{productId}/image")
    public ResponseEntity<ProductDTO> uploadImage(@RequestParam("image") MultipartFile image,@PathVariable Long productId)
            throws IOException {
        ProductDTO productDTO = productService.uploadImage(productId,image);
        return new ResponseEntity<>(productDTO,HttpStatus.OK);
    }
}
