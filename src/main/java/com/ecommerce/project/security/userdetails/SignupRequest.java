package com.ecommerce.project.security.userdetails;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    @NotBlank
    @Size(min = 3)
    private String username;

    @Email
    private String email;

    @NotBlank
    @Size(min = 4,max = 120)
    private String password;

    private Set<String> roles;




}
