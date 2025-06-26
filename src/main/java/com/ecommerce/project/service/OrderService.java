package com.ecommerce.project.service;

import com.ecommerce.project.dto.OrderDTO;

public interface OrderService {
    OrderDTO placeAnOrder(String paymentMethod,String emailId, Long addressId, String pgId, String pgName,
                          String pgStatus, Long addressId1, String pgResponseMessage);

}
