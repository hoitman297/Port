package com.portfolio.api.security;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

/**
 * "Secure" cookies work over http://localhost too — browsers treat localhost as a secure
 * context, so this doesn't require HTTPS for local FO/BO dev servers.
 *
 * SameSite=None (not Lax): once FO/BO are deployed, they live on a different domain than
 * this API (e.g. *.vercel.app frontend, a separate backend host), making every request
 * cross-site. SameSite=Lax cookies are withheld from cross-site fetch/XHR entirely — only
 * top-level navigations get them — which would silently break auth right after login in
 * production. None requires Secure, which is already set. Locally, FO/BO/backend are all
 * "localhost" (same site, different ports), so this has no effect on local dev.
 */
@Component
public class JwtCookieFactory {

    public static final String COOKIE_NAME = "portfolio_admin_token";

    public ResponseCookie create(String token, long maxAgeSeconds) {
        return ResponseCookie.from(COOKIE_NAME, token)
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .path("/")
            .maxAge(maxAgeSeconds)
            .build();
    }

    public ResponseCookie clear() {
        return ResponseCookie.from(COOKIE_NAME, "")
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .path("/")
            .maxAge(0)
            .build();
    }
}
