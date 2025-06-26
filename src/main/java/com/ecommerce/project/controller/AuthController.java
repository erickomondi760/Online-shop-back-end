package com.ecommerce.project.controller;

import com.ecommerce.project.model.Roles;
import com.ecommerce.project.model.RolesName;
import com.ecommerce.project.model.User;
import com.ecommerce.project.repository.RolesRepository;
import com.ecommerce.project.repository.UserRepository;
import com.ecommerce.project.security.userdetails.MessageResponse;
import com.ecommerce.project.security.userdetails.SignupRequest;
import com.ecommerce.project.security.JwtUtils;
import com.ecommerce.project.security.userdetails.LoginRequest;
import com.ecommerce.project.security.userdetails.LoginResponse;
import com.ecommerce.project.security.userdetails.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;


    @PostMapping("/signin")
    public ResponseEntity<Object> validateCredentials(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), loginRequest.getPassword()
            ));

        } catch (AuthenticationException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Wrong login credentials provided, try again");
            map.put("status", false);
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);

        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie responseCookie = jwtUtils.getResponseCookieFromUser(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(
                item -> item.getAuthority()).toList();

        LoginResponse loginResponse = new LoginResponse(userDetails.getId(),userDetails.getUsername(),roles);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(loginResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> signup(@Valid @RequestBody SignupRequest signupRequest){
        if(userRepository.existsByUsername(signupRequest.getUsername())){
            return ResponseEntity.badRequest().body(new MessageResponse(signupRequest
                    .getUsername()+" already exist"));
        }

        if(userRepository.existsByEmail(signupRequest.getEmail())){
            return ResponseEntity.badRequest().body(new MessageResponse(signupRequest
                    .getEmail()+" already exist"));
        }

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(encoder.encode(signupRequest.getPassword()));

        Set<String> strRole = signupRequest.getRoles();
        Set<Roles> roles = new HashSet<>();

        if(strRole == null){
            Roles role = roleRepository.findByRolesName(RolesName.ROLE_USER);
            roles.add(role);

            if(role == null){
                role = new Roles();
                role.setRolesName(RolesName.ROLE_USER);
                roles.add(role);
            }
        }else{
            strRole.forEach(myRole ->{
                switch(myRole){
                    case "admin":
                        Roles role = roleRepository.findByRolesName(RolesName.ROLE_ADMIN);
                        roles.add(role);
                        break;

                    case "seller":
                        Roles roleSeller = roleRepository.findByRolesName(RolesName.ROLE_SELLER);
                        roles.add(roleSeller);
                        break;

                    default:
                        Roles roleUser = roleRepository.findByRolesName(RolesName.ROLE_USER);
                        roles.add(roleUser);

                }
            });
        }
        user.setUserRoles(roles);
        userRepository.save(user);

        return new ResponseEntity<MessageResponse>(new MessageResponse("User successfully registered"),HttpStatus.OK);
    }

    @GetMapping("username")
    public ResponseEntity<String> getUsername(Authentication authentication){
        if(authentication != null)
            return ResponseEntity.ok().body(authentication.getName());

        return ResponseEntity.ok().body("No user is logged in");
    }

    @GetMapping("user")
    public ResponseEntity<?> getUser(Authentication authentication){
        if(authentication != null){
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            List<String> roles = userDetails.getAuthorities().stream().map(item ->
                    item.getAuthority()).toList();

            return new ResponseEntity<>(new LoginResponse(userDetails.getId(),userDetails.getUsername(),roles),
                    HttpStatus.OK);
        }
        return ResponseEntity.ok().body("User not found");
    }

    @GetMapping("signout")
    public ResponseEntity<?> signout(){
        ResponseCookie jwtCookie = jwtUtils.getJwtCleanCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,jwtCookie.toString()).body("You've been signed out");
    }
}


