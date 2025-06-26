package com.ecommerce.project.repository;

import com.ecommerce.project.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    @Query("select ci from CartItem ci where ci.cart.id = ?1 and ci.product.id = ?2")
    CartItem findByCartIdAndProductId(Long cartId, Long productId);

    @Modifying
    @Query("delete from CartItem ci where ci.product.id = ?1 and ci.cart.id = ?2")
    void deleteByProductIdAndCartId(Long productId, Long cartId);
}
