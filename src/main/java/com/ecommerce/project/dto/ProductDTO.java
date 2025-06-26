package com.ecommerce.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String productName;
    private String description;
    private Integer quantity;
    private double price;
    private double specialPrice;
    private String image;
    private double discount;

}
