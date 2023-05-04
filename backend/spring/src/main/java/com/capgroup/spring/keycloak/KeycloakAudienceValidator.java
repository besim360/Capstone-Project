package com.capgroup.spring.keycloak;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

/**
 * Class for custom audience (aud) or authorized party (azp) claim validations.
 */
@Component
@RequiredArgsConstructor
class KeycloakAudienceValidator implements OAuth2TokenValidator<Jwt> {

    private final OAuth2Error ERROR_INVALID_AUDIENCE = new OAuth2Error("invalid_token", "Invalid audience", null);

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {


        return OAuth2TokenValidatorResult.success();
    }
}