package com.ecommerce.project.service;

import com.ecommerce.project.dto.UserDTO;
import com.ecommerce.project.model.User;
import com.ecommerce.project.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<User> findUsers() {
        List<User> users = userRepository.findAll();
//        List<UserDTO> userDTOS = users.stream().map(user -> modelMapper.map(user, UserDTO.class))
//                .toList();
        return users;
    }
}
