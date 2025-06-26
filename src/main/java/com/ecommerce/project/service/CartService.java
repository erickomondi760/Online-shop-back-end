package com.ecommerce.project.service;

import com.ecommerce.project.dto.CartDTO;

import java.util.List;

public interface CartService {

    CartDTO addToCart(long productId,int quantity);

    List<CartDTO> findAll();

    List<CartDTO> findByEmailandId(String email, Long id);

    CartDTO updateCartItem(Long productId, int quantity);

    String deleteCartItem(Long productId, Long cartId);
}
