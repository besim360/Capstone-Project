package com.capgroup.spring.config;


import com.capgroup.spring.controller.AccessController;
import com.capgroup.spring.keycloak.KeycloakJwtAuthenticationConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


/**
 * Spring's security configuration for which subdomains will be checked for authentication and authorization through Keycloak.
 * This is only used for the Java API when a user wants to update, add, or delete an article. Every other instance
 * will be handled through the Python API.
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration{
    private final KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{ //https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html

        http.sessionManagement(smc -> {
            smc.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                });
        http.authorizeRequests(authorize -> {
            authorize.requestMatchers("/article/admin/**").access("@accessController.checkAccess()");
        });
        http.oauth2ResourceServer(arsc -> {
            arsc.jwt().jwtAuthenticationConverter(keycloakJwtAuthenticationConverter);
        });

        return http.build();
    }
    @Bean
    AccessController accessController() {
        return new AccessController();
    }
}
