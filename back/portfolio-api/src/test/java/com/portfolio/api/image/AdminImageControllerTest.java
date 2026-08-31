package com.portfolio.api.image;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.portfolio.api.domain.Admin;
import com.portfolio.api.repository.AdminRepository;
import com.portfolio.api.security.JwtCookieFactory;
import com.portfolio.api.security.JwtTokenProvider;

import jakarta.servlet.http.Cookie;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private Cookie authCookie;

    @BeforeEach
    void setUp() {
        adminRepository.save(new Admin("image-admin", passwordEncoder.encode("pw"), "image@example.com"));
        authCookie = new Cookie(JwtCookieFactory.COOKIE_NAME, jwtTokenProvider.generateToken("image-admin"));
    }

    @AfterEach
    void tearDown() {
        adminRepository.deleteAll();
    }

    @Test
    void rejectsNonImageFilesWith400() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file", "resume.pdf", "application/pdf", "not an image".getBytes()
        );

        mockMvc.perform(multipart("/api/admin/images").file(file).cookie(authCookie))
            .andExpect(status().isBadRequest());
    }

    @Test
    void uploadEndpointRequiresAuthentication() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file", "thumb.png", "image/png", "fake-bytes".getBytes()
        );

        mockMvc.perform(multipart("/api/admin/images").file(file))
            .andExpect(status().isUnauthorized());
    }
}
