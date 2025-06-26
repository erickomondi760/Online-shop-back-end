package com.ecommerce.project.controller;

import com.ecommerce.project.dto.OrderDTO;
import com.ecommerce.project.dto.OrderRequestDTO;
import com.ecommerce.project.security.userdetails.AuthUtil;
import com.ecommerce.project.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class OrderController {

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private OrderService orderService;

    @PostMapping("orders/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> placeAnOrder(@PathVariable String paymentMethod,
                                                 @RequestBody OrderRequestDTO orderRequestDTO){
        OrderDTO orderDTO = orderService.placeAnOrder(paymentMethod,
                authUtil.getloggedInEmail(),
                orderRequestDTO.getAddressId(),
                orderRequestDTO.getPGId(),
                orderRequestDTO.getPGName(),
                orderRequestDTO.getPGStatus(),
                orderRequestDTO.getAddressId(),
                orderRequestDTO.getPGResponseMessage());
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDTO);
    }
}
