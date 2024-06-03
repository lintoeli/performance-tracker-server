package com.diversolab.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Deshabilitar CSRF
            .authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll())
            .httpBasic(); // Este es un ejemplo, ajusta la autenticación según tus necesidades
        return http.build();
    }
}