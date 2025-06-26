package com.ecommerce.project.controller;


import com.ecommerce.project.security.userdetails.AuthUtil;
import com.ecommerce.project.dto.CartDTO;
import com.ecommerce.project.exceptionHandler.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.repository.CartRepository;
import com.ecommerce.project.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private CartRepository cartRepository;

    @PostMapping("carts/products/{productId}/{quantity}")
    public ResponseEntity<CartDTO> addItemToCart(@PathVariable long productId, @PathVariable int quantity){
        CartDTO cartDTO = cartService.addToCart(productId,quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartDTO);
    }

    @GetMapping("carts")
    public ResponseEntity<List<CartDTO>> getAllCarts(){
        return ResponseEntity.status(HttpStatus.OK).body(cartService.findAll());
    }

    @GetMapping("carts/users/cart")
    public ResponseEntity<List<CartDTO>> getUsercart(){
        String email = authUtil.getloggedInEmail();
        Cart cart = cartRepository.findByEmail(email);
        if(cart == null)
            throw new ResourceNotFoundException(email+" does not have any cart");
        List<CartDTO> cartDTOs = cartService.findByEmailandId(email,cart.getId());
        return new ResponseEntity<>(cartDTOs,HttpStatus.OK);
    }

    @PostMapping("carts/products/quantity/{productId}/{operation}")
    public ResponseEntity<CartDTO> updateCartItem(@PathVariable Long productId,@PathVariable String operation){
        CartDTO cartDTO = cartService.updateCartItem(productId,operation.
                equalsIgnoreCase("decrease") ? -1:1);
        return new ResponseEntity<>(cartDTO,HttpStatus.OK);
    }

    @DeleteMapping("carts/products/{productId}/{cartId}")
    public String deleteCartItem(@PathVariable Long productId,@PathVariable Long cartId){
        return cartService.deleteCartItem(productId,cartId);
    }
}
