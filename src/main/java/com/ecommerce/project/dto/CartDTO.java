package com.ecommerce.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private Long id;
    private List<ProductDTO> cartItems = new ArrayList<>();
    private double totalPrice;
}
