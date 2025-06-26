package com.ecommerce.project.service;

import com.ecommerce.project.dto.CartDTO;
import com.ecommerce.project.dto.ProductDTO;
import com.ecommerce.project.dto.ProductResponse;
import com.ecommerce.project.exceptionHandler.APIException;
import com.ecommerce.project.exceptionHandler.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.repository.CartRepository;
import com.ecommerce.project.repository.CategoryRepository;
import com.ecommerce.project.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;
    private final FileService fileService;

    @Value("${project.images}")
    String path;


    @Autowired
    public ProductServiceImpl(CategoryRepository categoryRepository,ProductRepository productRepository,
                              CartRepository cartRepository,ModelMapper modelMapper,FileService fileService) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.fileService = fileService;
        this.cartRepository = cartRepository;
    }

    @Override
    @Transactional
    public ProductDTO saveProduct(Product product, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException("Supplied category id does not exist"));

        List<Product> returnedProduct = productRepository.findByProductNameIgnoreCase(product.getProductName());
        if(!returnedProduct.isEmpty())
            throw new APIException("Product with similar name exists");

        product.setCategory(category);
        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct,ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {
        List<Product> products = productRepository.findAll();
        
        Sort sortByandOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending():Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByandOrder);
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductDTO> productDTOS = productPage.stream().map(product ->
                modelMapper.map(product,ProductDTO.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());


        return productResponse;
    }

    @Override
    public ProductResponse getProductsByCategory(Long id,
     Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = categoryRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Supplied category id does not exist"));

        Sort sort = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> productPage = productRepository.findByCategoryOrderByPriceAsc(category,pageable);
        List<ProductDTO> productDTOS = productPage.stream().map(product ->
                modelMapper.map(product,ProductDTO.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());

        return productResponse;
    }

    @Override
    public ProductResponse getProductsByKeyword(String keyword,
    Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase("%"+keyword+"%",pageable);
        List<ProductDTO> productDTOS = productPage.stream().map(product ->
                modelMapper.map(product,ProductDTO.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());

        return productResponse;
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Product product, Long productId) {
        Product returnedProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in the database"));

        returnedProduct.setProductName(product.getProductName());
        returnedProduct.setDescription(product.getDescription());
        returnedProduct.setPrice(product.getPrice());
        returnedProduct.setDiscount(product.getDiscount());
        returnedProduct.setQuantity(product.getQuantity());
        returnedProduct.setSpecialPrice(product.getPrice() - (product.getPrice() * (product.getDiscount())/100));
        Product savedProduct = productRepository.save(returnedProduct);

        List<Cart> carts = cartRepository.findByProductId(productId);

        List<CartDTO> cartDTOS = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
            List<ProductDTO> productDTOS = cart.getCartItems().stream().map(item ->
                modelMapper.map(item.getProduct(),ProductDTO.class)
             ).toList();
            cartDTO.setCartItems(productDTOS);
            return cartDTO;
        }).toList();

        return modelMapper.map(savedProduct,ProductDTO.class);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        Product returnedProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in the database"));
        productRepository.delete(returnedProduct);

    }

    @Override
    @Transactional
    public ProductDTO uploadImage(Long productId, MultipartFile image) throws IOException {
        Product returnedProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in the database"));

        String productImage = fileService.uploadProductImage(path,image);
        returnedProduct.setImage(productImage);

        return modelMapper.map(productRepository.save(returnedProduct),ProductDTO.class);
    }


}
