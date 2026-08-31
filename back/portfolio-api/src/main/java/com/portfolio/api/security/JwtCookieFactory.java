package com.portfolio.api.security;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

/**
 * "Secure" cookies work over http://localhost too — browsers treat localhost as a secure
 * context, so this doesn't require HTTPS for local FO/BO dev servers.
 */
@Component
public class JwtCookieFactory {

    public static final String COOKIE_NAME = "portfolio_admin_token";

    public ResponseCookie create(String token, long maxAgeSeconds) {
        return ResponseCookie.from(COOKIE_NAME, token)
            .httpOnly(true)
            .secure(true)
            .sameSite("Lax")
            .path("/")
            .maxAge(maxAgeSeconds)
            .build();
    }

    public ResponseCookie clear() {
        return ResponseCookie.from(COOKIE_NAME, "")
            .httpOnly(true)
            .secure(true)
            .sameSite("Lax")
            .path("/")
            .maxAge(0)
            .build();
    }
}
