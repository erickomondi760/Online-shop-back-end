package com.ecommerce.project.model;


import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Address{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, message = "City name must be at least 2 characters")
    @NotBlank
    private String street;

    @Size(min = 4, message = "Building name must be at least 4 characters")
    @NotBlank
    private String building;

    @Size(min = 4, message = "City name must be at least 4 characters")
    @NotBlank
    private String city;

    @Size(min = 4, message = "Country name must be at least 4 characters")
    @NotBlank
    private String country;

    @Size(min = 4, message = "ZIP code must be at least 4 characters")
    @NotBlank
    private String zipCode;
    @ManyToMany(mappedBy = "addresses",cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH})
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "address")
    private List<Order> orders = new ArrayList<>();



}
