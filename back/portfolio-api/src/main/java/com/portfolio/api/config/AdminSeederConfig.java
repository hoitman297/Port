package com.portfolio.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.portfolio.api.domain.Admin;
import com.portfolio.api.repository.AdminRepository;

/**
 * Seeds one local-dev admin account (idempotent) so POST /api/auth/login has something to
 * authenticate against. No default id/password here — app.admin.* must be set in
 * application-local.yml (gitignored), never hardcoded in source.
 */
@Configuration
@Profile("local")
public class AdminSeederConfig {

    @Bean
    public CommandLineRunner seedAdmin(
        AdminRepository adminRepository,
        PasswordEncoder passwordEncoder,
        @Value("${app.admin.username}") String username,
        @Value("${app.admin.password}") String rawPassword,
        @Value("${app.admin.email}") String email
    ) {
        return args -> {
            if (!adminRepository.existsByUsername(username)) {
                adminRepository.save(new Admin(username, passwordEncoder.encode(rawPassword), email));
            }
        };
    }
}
