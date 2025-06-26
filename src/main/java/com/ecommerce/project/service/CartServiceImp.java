package com.ecommerce.project.service;

import com.ecommerce.project.security.userdetails.AuthUtil;
import com.ecommerce.project.dto.CartDTO;
import com.ecommerce.project.dto.ProductDTO;
import com.ecommerce.project.exceptionHandler.APIException;
import com.ecommerce.project.exceptionHandler.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.CartItem;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.repository.CartItemRepository;
import com.ecommerce.project.repository.CartRepository;
import com.ecommerce.project.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImp implements CartService{
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthUtil authUtil;


    @Override
    public CartDTO addToCart(long productId, int quantity) {
        Product product = productRepository.findById(productId).
                orElseThrow(() ->new ResourceNotFoundException("Product of id :"+productId +" not found"));
        Cart cart = createCart();

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);
        if(cartItem != null){
            throw new APIException("Product already exist in the cart");
        }

        if(product.getQuantity() == 0){
            throw new APIException("Product is out of stock");
        }

        if(product.getQuantity() < quantity){
            throw new APIException("Available stock is:"+product.getQuantity());
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setCart(cart);
        newCartItem.setProduct(product);
        newCartItem.setQuantity(quantity);
        newCartItem.setProductPrice(product.getPrice());

        CartItem savedCartItem = cartItemRepository.save(newCartItem);
        System.out.println("cart:"+savedCartItem.getCart().getCartItems());


        cart.setTotalPrice(cart.getTotalPrice() + (quantity * product.getPrice()));
        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);

        List<ProductDTO> productDTOS = cart.getCartItems().stream().map(item -> {
            ProductDTO dto = modelMapper.map(item.getProduct(),ProductDTO.class);
            dto.setQuantity(item.getQuantity());
            return dto;
        }).toList();

        cartDTO.setCartItems(productDTOS);
        return cartDTO;
    }

    @Override
    public List<CartDTO> findAll() {
        List<Cart> carts = cartRepository.findAll();
        return carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
            List<ProductDTO> productDTOS = cart.getCartItems().stream().map(prod ->{
                ProductDTO productDTO = modelMapper.map(prod.getProduct(),ProductDTO.class);
                productDTO.setQuantity(prod.getQuantity());
                return productDTO;
                    }).toList();
            cartDTO.setCartItems(productDTOS);
            return cartDTO;
        }).toList();
    }

    @Override
    public List<CartDTO> findByEmailandId(String email, Long id) {
        Cart cart = cartRepository.findByEmailandId(email, id);
        cart.getCartItems().forEach(cartItem -> cartItem.getProduct().setQuantity(cartItem.getQuantity()));
        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);

        List<CartDTO> cartDTOList = new ArrayList<>();

        List<ProductDTO> productDTOS = cart.getCartItems().stream().map(cartItem ->
                modelMapper.map(cartItem.getProduct(),ProductDTO.class)).toList();
        cartDTO.setCartItems(productDTOS);
        cartDTOList.add(cartDTO);

        return cartDTOList;
    }

    @Transactional
    @Override
    public CartDTO updateCartItem(Long productId, int quantity) {
        String userEmail = authUtil.getloggedInEmail();
        Cart cart = cartRepository.findByEmail(userEmail);

        Product product = productRepository.findById(productId).
                orElseThrow(() ->new ResourceNotFoundException("Product of id :"+productId +" not found"));

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);

        if(product.getQuantity() == 0){
            throw new APIException("Product is out of stock");
        }

        if(product.getQuantity() < quantity){
            throw new APIException("Available stock is:"+product.getQuantity());
        }

        cartItem.setQuantity(cartItem.getQuantity() + quantity);

        CartDTO cartDTO = null;
        if(cartItem.getQuantity() == 0){
            cart.getCartItems().remove(cartItem);
            cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getQuantity() * cartItem.getProductPrice()));
            cartRepository.save(cart);
        }else{
        cartItemRepository.save(cartItem);

        cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getQuantity() * cartItem.getProductPrice()));
        cartRepository.save(cart);

        List<ProductDTO> productDTOS = cart.getCartItems().stream().map(item -> {
            ProductDTO prodDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
            prodDTO.setQuantity(item.getQuantity());
            return prodDTO;
        }).toList();

        cart = cartRepository.findById(cart.getId()).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        cartDTO = modelMapper.map(cart,CartDTO.class);
        cartDTO.setCartItems(productDTOS);
        }
        return cartDTO;
    }

    @Transactional
    @Override
    public String deleteCartItem(Long productId, Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        Product product = productRepository.findById(productId).
                orElseThrow(() ->new ResourceNotFoundException("Product of id :"+productId +" not found"));

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cartId,productId);
        if(cartItem == null)
            throw new ResourceNotFoundException("Product does not exist in the cart");

        cartItemRepository.deleteByProductIdAndCartId(productId,cartId);

        cart.setTotalPrice(cart.getTotalPrice() - cartItem.getQuantity() * cartItem.getProductPrice());
        cartRepository.save(cart);
        return "Product successfully removed from cart";
    }

    private Cart createCart(){
        Cart cart = cartRepository.findByEmail(authUtil.getloggedInEmail());
        if(cart != null){
            return cart;
        }

        Cart cart1 = new Cart();
        cart1.setUser(authUtil.getLoggedInUser());
        cart1.setTotalPrice(0.00);
        return cartRepository.save(cart1);
    }
}
