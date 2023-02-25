package com.capgroup.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring's security configuration for which subdomains will be checked for authentication and authorization through Keycloak.
 * This is only used for the Java API when a user wants to update, add, or delete an article. Every other instance
 * will be handled through the Python API.
 */
@Configuration
@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration{
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{ //https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html
        http
                .securityMatcher("/article/admin/**") //should only check authorization within /article/admin
                .authorizeRequests(authorize -> authorize
                        //.requestMatchers("/search/**").permitAll()
                        .requestMatchers("/article/admin/**").hasRole("admin")
                        .anyRequest().authenticated()
                ).oauth2ResourceServer(oauth2 -> oauth2.jwt());
                //http.httpBasic().disable(); //temporary stopgap when debugging
        return http.build();
    }
}
