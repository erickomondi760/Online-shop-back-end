package com.ecommerce.project.service;

import com.ecommerce.project.dto.UserDTO;
import com.ecommerce.project.model.User;

import java.util.List;

public interface UserService {
    List<User> findUsers();
}
