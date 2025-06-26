package com.ecommerce.project.security.userdetails;

import com.ecommerce.project.model.User;
import com.ecommerce.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    @Autowired
    private UserRepository userRepository;

    public String getloggedInEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());

        if(user == null)
            throw new UsernameNotFoundException("User does not exist");;
        return user.getEmail();
    }

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());

        if(user == null)
            throw new UsernameNotFoundException(user.getEmail() + " does not exist");
        return user;
    }

    public Long getloggedInId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());

        if(user == null)
            throw new UsernameNotFoundException(user.getEmail() + " does not exist");;
        return user.getId();
    }
}
