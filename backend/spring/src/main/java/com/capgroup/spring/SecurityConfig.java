package com.capgroup.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig{
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{ //https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html
        /*http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/search/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("admin")
                        .anyRequest().denyAll()
                );*/
                http.httpBasic().disable();
        return http.build();
    }
}
