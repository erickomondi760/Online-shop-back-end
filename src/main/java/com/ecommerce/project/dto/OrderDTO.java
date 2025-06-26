package com.ecommerce.project.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long id;
    private String email;
    private LocalDate date;
    private String status;
    private List<OrderItemDTO> orderItems = new ArrayList<>();
    private Long addressId;
    private PaymentDTO payment;
    private double orderTotal;
}
