package com.portfolio.api.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.api.auth.dto.LoginRequest;
import com.portfolio.api.auth.dto.LoginResponse;
import com.portfolio.api.auth.dto.MeResponse;
import com.portfolio.api.domain.Admin;
import com.portfolio.api.exception.AuthenticationRequiredException;
import com.portfolio.api.repository.AdminRepository;
import com.portfolio.api.security.JwtCookieFactory;
import com.portfolio.api.security.JwtTokenProvider;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AdminRepository adminRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtCookieFactory jwtCookieFactory;

    public AuthController(
        AuthService authService,
        AdminRepository adminRepository,
        JwtTokenProvider jwtTokenProvider,
        JwtCookieFactory jwtCookieFactory
    ) {
        this.authService = authService;
        this.adminRepository = adminRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtCookieFactory = jwtCookieFactory;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Admin admin = authService.authenticate(request.username(), request.password());
        String token = jwtTokenProvider.generateToken(admin.getUsername());
        long expiresIn = jwtTokenProvider.getAccessTokenExpirationSeconds();
        ResponseCookie cookie = jwtCookieFactory.create(token, expiresIn);

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(new LoginResponse(admin.getUsername(), expiresIn));
    }

    @GetMapping("/me")
    public MeResponse me(Authentication authentication) {
        if (!isAuthenticated(authentication)) {
            throw new AuthenticationRequiredException("로그인이 필요합니다.");
        }

        Admin admin = adminRepository.findByUsername(authentication.getName())
            .orElseThrow(() -> new AuthenticationRequiredException("로그인이 필요합니다."));

        return new MeResponse(admin.getUsername(), admin.getEmail());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = jwtCookieFactory.clear();
        return ResponseEntity.noContent()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .build();
    }

    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null
            && authentication.isAuthenticated()
            && !(authentication instanceof AnonymousAuthenticationToken);
    }
}
