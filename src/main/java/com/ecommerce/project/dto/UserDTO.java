package com.ecommerce.project.dto;

import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.model.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private String password;
    private Set<Roles> userRoles = new HashSet<>();
    private List<Product> products = new ArrayList<>();
    private List<Address> addresses = new ArrayList<>();
    private Cart cart;
}
