package com.ecommerce.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemsDTO {
    private Long id;
    private CartDTO cart;
    private ProductDTO product;
    private double productPrice;
    private  double discount;
    private  int quantity;
}
