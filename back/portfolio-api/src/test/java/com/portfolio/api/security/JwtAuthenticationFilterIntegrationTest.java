package com.portfolio.api.security;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.portfolio.api.domain.Admin;
import com.portfolio.api.repository.AdminRepository;

import jakarta.servlet.http.Cookie;

/** Core rule: /api/admin/** must reject requests with no (or an invalid) JWT cookie, and accept a valid one. */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class JwtAuthenticationFilterIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @AfterEach
    void tearDown() {
        adminRepository.deleteAll();
    }

    @Test
    void adminEndpointRejectsRequestsWithNoCookie() throws Exception {
        mockMvc.perform(get("/api/admin/tech-stacks"))
            .andExpect(status().isUnauthorized())
            .andExpect(content().string(containsString("인증이 필요합니다.")));
    }

    @Test
    void adminEndpointRejectsAnInvalidCookie() throws Exception {
        mockMvc.perform(get("/api/admin/tech-stacks").cookie(new Cookie(JwtCookieFactory.COOKIE_NAME, "not-a-valid-jwt")))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void adminEndpointAcceptsAValidCookie() throws Exception {
        adminRepository.save(new Admin("tester", passwordEncoder.encode("pw"), "tester@example.com"));
        String token = jwtTokenProvider.generateToken("tester");

        mockMvc.perform(get("/api/admin/tech-stacks").cookie(new Cookie(JwtCookieFactory.COOKIE_NAME, token)))
            .andExpect(status().isOk());
    }
}
