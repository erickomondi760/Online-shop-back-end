package com.ecommerce.project.service;

import com.ecommerce.project.dto.OrderDTO;
import com.ecommerce.project.dto.OrderItemDTO;
import com.ecommerce.project.exceptionHandler.APIException;
import com.ecommerce.project.exceptionHandler.ResourceNotFoundException;
import com.ecommerce.project.model.*;
import com.ecommerce.project.repository.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRespository orderItemRespository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Transactional
    @Override
    public OrderDTO placeAnOrder(String paymentMethod, String emailId, Long addressId, String pgId,
                                 String pgName, String pgStatus, Long addressId1, String pgResponseMessage) {
        //Fetch user cart
        Cart cart = cartRepository.findByEmail(emailId);
        if(cart == null){
            throw new ResourceNotFoundException("Cart does not exist");
        }

        //Fetch shipping address
        Address address = addressRepository.findById(addressId).orElseThrow(()->
                new ResourceNotFoundException("Address not found"));

        //Create an order
        Order order = new Order();
        order.setOrderTotal(cart.getTotalPrice());
        order.setDate(LocalDate.now());
        order.setAddress(address);
        order.setEmail(emailId);
        order.setStatus(pgStatus);

        final Order savedOrder = orderRepository.save(order);


        //Create order items
        List<OrderItem> orderItems = new ArrayList<>();

        cart.getCartItems().forEach(cartItem ->{
            OrderItem orderItem = new OrderItem();
            orderItem.setAmount(cartItem.getProductPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(cartItem.getProduct());

            orderItems.add(orderItem);
        });

        orderItemRespository.saveAll(orderItems);


        //Create payment
        Payment payment = new Payment(paymentMethod,pgId,pgResponseMessage,pgStatus,pgName);
        payment.setOrder(savedOrder);
        Payment savedPayment = paymentRepository.save(payment);


        order.setOrderItems(orderItems);
        order.setPayment(savedPayment);
        Order newSavedOrder = orderRepository.save(order);

        //Update stock
        orderItems.forEach(item->{
            int qty = item.getQuantity();
            Product product = item.getProduct();
            if(product.getQuantity() < qty){
                throw new APIException(product.getProductName()+" has insufficient stock.Current stock = "
                        +product.getQuantity());
            }
            product.setQuantity(product.getQuantity() - qty);
            productRepository.save(product);
        });


        OrderDTO orderDTO = modelMapper.map(newSavedOrder,OrderDTO.class);
        List<OrderItemDTO> orderItemDTOS = orderItems.stream().map(
                orderItem -> modelMapper.map(orderItem,OrderItemDTO.class)
        ).toList();
        orderDTO.setOrderItems(orderItemDTOS);

        return orderDTO;
    }
}
