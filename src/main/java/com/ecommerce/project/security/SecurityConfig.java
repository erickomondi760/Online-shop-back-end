package com.ecommerce.project.security;

import com.ecommerce.project.repository.UserRepository;
import com.ecommerce.project.security.userdetails.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private AuthenticationEntryJwt authenticationEntryJwt;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    @Lazy
    private AuthenticationTokenFilter authFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public CommandLineRunner runner(){
//        return args -> {
//
//            if(!userRepository.existsByUsername("erick") && !userRepository.existsByUsername("mike")
//            && !userRepository.existsByUsername("chris")) {
//
//
//                User user = new User();
//                user.setUsername("erick");
//                user.setEmail("test@gmail.com");
//                user.setPassword(encoder().encode("erick"));
//
//                Roles roles1 = new Roles();
//                roles1.setRolesName(RolesName.ROLE_USER);
//                user.getUserRoles().add(roles1);
//
//                User user2 = new User();
//                user2.setUsername("mike");
//                user2.setEmail("test1@gmail.com");
//                user2.setPassword(encoder().encode("mike"));
//
//                Roles roles2 = new Roles();
//                roles2.setRolesName(RolesName.ROLE_ADMIN);
//                user2.getUserRoles().add(roles2);
//
//                User user3 = new User();
//                user3.setUsername("chris");
//                user3.setEmail("test3@gmail.com");
//                user3.setPassword(encoder().encode("chris"));
//
//                Roles roles3 = new Roles();
//                roles3.setRolesName(RolesName.ROLE_SELLER);
//                user3.getUserRoles().add(roles3);
//
//                userRepository.save(user);
//                userRepository.save(user2);
//                userRepository.save(user3);
//
//            }
//        };
//    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(encoder());
        return provider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(config ->
                config.requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/v1/api-docs/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
//                        .requestMatchers("/api/public/**").hasRole("USER")
//                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/api/test/**").permitAll()

                        .anyRequest().authenticated());
        httpSecurity.authenticationProvider(authenticationProvider());

        httpSecurity.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.exceptionHandling(exce ->
                exce.authenticationEntryPoint(authenticationEntryJwt));

        httpSecurity.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.headers(headers -> headers.frameOptions(
                frameOptions -> frameOptions.sameOrigin()
        ));


        return httpSecurity.build();
    }

    @Bean
    public WebSecurityCustomizer customizer(){
        return (web -> web.ignoring().requestMatchers(
                "/v1/api/**",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"));
    }

}
