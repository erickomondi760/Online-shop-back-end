package com.ecommerce.project.configurations;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class ConfigClass {

    @Bean
    public ModelMapper getModelMapper(){
        return new ModelMapper();
    }

}
