package com.ecommerce.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Long id;
    private ProductDTO productDTO;
    private OrderDTO orderDTO;
    private int quantity;
    private double discount;
    private double amount;
}
