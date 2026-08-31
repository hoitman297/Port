package com.portfolio.api.project;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.portfolio.api.domain.Admin;
import com.portfolio.api.repository.AdminRepository;
import com.portfolio.api.repository.ProjectRepository;
import com.portfolio.api.repository.TechStackRepository;
import com.portfolio.api.security.JwtCookieFactory;
import com.portfolio.api.security.JwtTokenProvider;

import jakarta.servlet.http.Cookie;
import tools.jackson.databind.json.JsonMapper;

/**
 * End-to-end happy path across project -> feature -> troubleshooting, plus the two
 * cross-cutting rules that are easy to get wrong: null troubleshooting until one is
 * added, and the tech-stack delete conflict tracking the right project id.
 *
 * <p>Deliberately NOT @Transactional: each MockMvc call should get its own real
 * transaction/persistence context, same as production (open-in-view is disabled).
 * Wrapping the whole test in one shared transaction caused a spurious
 * TransientPropertyValueException from stale entity references leaking across what
 * should be independent "requests" — cleaning up in @AfterEach instead avoids that.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProjectCrudFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TechStackRepository techStackRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private JsonMapper jsonMapper;

    private Cookie authCookie;

    @BeforeEach
    void setUp() {
        adminRepository.save(new Admin("crud-admin", passwordEncoder.encode("pw"), "crud@example.com"));
        String token = jwtTokenProvider.generateToken("crud-admin");
        authCookie = new Cookie(JwtCookieFactory.COOKIE_NAME, token);
    }

    @AfterEach
    void tearDown() {
        projectRepository.deleteAll();
        techStackRepository.deleteAll();
        adminRepository.deleteAll();
    }

    @Test
    void creatingAProjectWithoutATitleFails() throws Exception {
        mockMvc.perform(post("/api/admin/projects").cookie(authCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"summary\":\"no title\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors.title").exists());
    }

    @Test
    void fullProjectFeatureTroubleshootingFlow() throws Exception {
        MvcResult techStackResult = mockMvc.perform(post("/api/admin/tech-stacks").cookie(authCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Spring Boot\",\"category\":\"Backend\"}"))
            .andExpect(status().isCreated())
            .andReturn();
        long techStackId = jsonMapper.readTree(techStackResult.getResponse().getContentAsString()).get("id").asLong();

        String projectBody = String.format(
            "{\"title\":\"중고거래 플랫폼\",\"summary\":\"요약\",\"techStackIds\":[%d]}", techStackId
        );
        MvcResult projectResult = mockMvc.perform(post("/api/admin/projects").cookie(authCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.techStacks", hasSize(1)))
            .andReturn();
        long projectId = jsonMapper.readTree(projectResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(get("/api/projects"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(projectId));

        MvcResult featureResult = mockMvc.perform(post("/api/admin/projects/" + projectId + "/features").cookie(authCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"실시간 알림\",\"description\":\"설명\",\"reason\":\"이유\",\"sortOrder\":1}"))
            .andExpect(status().isCreated())
            .andReturn();
        long featureId = jsonMapper.readTree(featureResult.getResponse().getContentAsString()).get("id").asLong();

        // troubleshooting must be an explicit null (not omitted) before one exists, per the API spec
        mockMvc.perform(get("/api/projects/" + projectId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.features[0].troubleshooting").value(nullValue()));

        mockMvc.perform(post("/api/admin/features/" + featureId + "/troubleshootings").cookie(authCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"problem\":\"문제\",\"analysis\":\"분석\",\"action\":\"실행\",\"result\":\"결과\"}"))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/api/projects/" + projectId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.features[0].troubleshooting.problem").value("문제"));

        mockMvc.perform(delete("/api/admin/tech-stacks/" + techStackId).cookie(authCookie))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.usedByProjectIds[0]").value(projectId));

        mockMvc.perform(put("/api/admin/projects/" + projectId).cookie(authCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"중고거래 플랫폼 v2\",\"techStackIds\":[]}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("중고거래 플랫폼 v2"))
            .andExpect(jsonPath("$.techStacks", hasSize(0)));

        mockMvc.perform(delete("/api/admin/tech-stacks/" + techStackId).cookie(authCookie))
            .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/admin/projects/" + projectId).cookie(authCookie))
            .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/projects/" + projectId))
            .andExpect(status().isNotFound());
    }
}
