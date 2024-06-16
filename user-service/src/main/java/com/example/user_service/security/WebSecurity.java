package com.example.user_service.security;

import com.example.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity {

    private final UserService userService;
    private final Environment env;
    private final ObjectPostProcessor<Object> objectObjectPostProcessor;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/**").permitAll()
                        .anyRequest().permitAll())
                .addFilter(getAuthenticationFilter())
                .headers((headers) -> headers.frameOptions((frame) -> frame.disable()));
        return http.build();
    }




    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationManagerBuilder builder = new AuthenticationManagerBuilder(objectObjectPostProcessor);
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(userService, env);
        authenticationFilter.setAuthenticationManager(authenticationManager(builder));
        return authenticationFilter;
    }


    private AuthenticationManager authenticationManager(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
        return builder.build();
    }

}
