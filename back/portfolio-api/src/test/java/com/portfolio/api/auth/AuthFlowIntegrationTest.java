package com.portfolio.api.auth;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.portfolio.api.domain.Admin;
import com.portfolio.api.repository.AdminRepository;
import com.portfolio.api.security.JwtCookieFactory;

import jakarta.servlet.http.Cookie;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

/**
 * Exercises the full login -> me -> logout cycle end to end, cookie included.
 * Cleans up in @AfterEach (rather than wrapping the test in @Transactional) so each
 * MockMvc call gets its own independent transaction, matching production behavior.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        adminRepository.save(new Admin("admin", passwordEncoder.encode("secret123"), "admin@example.com"));
    }

    @AfterEach
    void tearDown() {
        adminRepository.deleteAll();
    }

    @Test
    void loginFailsWithWrongPassword() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin\",\"password\":\"wrong\"}"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void meFailsWithoutLogin() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void logoutRequiresAuthentication() throws Exception {
        mockMvc.perform(post("/api/auth/logout"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void fullLoginMeLogoutCycleWorks() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin\",\"password\":\"secret123\"}"))
            .andExpect(status().isOk())
            .andExpect(cookie().exists(JwtCookieFactory.COOKIE_NAME))
            .andExpect(cookie().httpOnly(JwtCookieFactory.COOKIE_NAME, true))
            .andExpect(cookie().secure(JwtCookieFactory.COOKIE_NAME, true))
            .andExpect(jsonPath("$.username").value("admin"))
            .andExpect(content().string(org.hamcrest.Matchers.not(containsString("accessToken"))))
            .andReturn();

        Cookie authCookie = loginResult.getResponse().getCookie(JwtCookieFactory.COOKIE_NAME);
        assertNotNull(authCookie);
        assertTrue(authCookie.getValue().length() > 0);

        mockMvc.perform(get("/api/auth/me").cookie(authCookie))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("admin"))
            .andExpect(jsonPath("$.email").value("admin@example.com"));

        MvcResult logoutResult = mockMvc.perform(post("/api/auth/logout").cookie(authCookie))
            .andExpect(status().isNoContent())
            .andReturn();

        Cookie clearedCookie = logoutResult.getResponse().getCookie(JwtCookieFactory.COOKIE_NAME);
        assertNotNull(clearedCookie);
        assertTrue(clearedCookie.getMaxAge() == 0);
    }
}
