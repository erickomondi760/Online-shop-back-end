package com.ecommerce.project.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    private Long paymentId;
    private String paymentMethod;
    private String pGId;
    private String pGResponseMessage;
    private String pGStatus;
    private String pGName;
    private Long addressId;
}
