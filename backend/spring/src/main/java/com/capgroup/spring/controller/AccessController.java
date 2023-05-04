package com.capgroup.spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Generic custom access check on request level.
 */
@Slf4j
public class AccessController {

    /**
     * Generic custom access check on request level.
     * @return boolean for success/failure
     */
    public boolean checkAccess() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        log.info("Check access for username={} path={}", auth.getName(), requestAttributes.getRequest().getRequestURI());

        return true;
    }
}