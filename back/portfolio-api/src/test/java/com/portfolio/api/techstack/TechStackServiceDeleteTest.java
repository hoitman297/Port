package com.portfolio.api.techstack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.portfolio.api.domain.Project;
import com.portfolio.api.domain.ProjectTechStack;
import com.portfolio.api.domain.TechStack;
import com.portfolio.api.exception.TechStackInUseException;
import com.portfolio.api.repository.ProjectRepository;
import com.portfolio.api.repository.ProjectTechStackRepository;
import com.portfolio.api.repository.TechStackRepository;

/** Core rule: DELETE on a tech stack still referenced by a project must fail with the offending project ids. */
@DataJpaTest
@ActiveProfiles("test")
class TechStackServiceDeleteTest {

    @Autowired
    private TechStackRepository techStackRepository;

    @Autowired
    private ProjectTechStackRepository projectTechStackRepository;

    @Autowired
    private ProjectRepository projectRepository;

    private TechStackService techStackService;

    @BeforeEach
    void setUp() {
        techStackService = new TechStackService(techStackRepository, projectTechStackRepository);
    }

    @Test
    void deletingAnUnusedTechStackSucceeds() {
        TechStack techStack = techStackRepository.save(new TechStack("Redis", "Backend"));

        techStackService.delete(techStack.getId());

        assertTrue(techStackRepository.findById(techStack.getId()).isEmpty());
    }

    @Test
    void deletingATechStackReferencedByAProjectThrowsConflictWithProjectIds() {
        TechStack techStack = techStackRepository.save(new TechStack("Spring Boot", "Backend"));
        Project project = projectRepository.save(
            new Project("중고거래 플랫폼", "요약", null, null, null, null, null)
        );
        projectTechStackRepository.save(new ProjectTechStack(project, techStack));

        TechStackInUseException ex = assertThrows(
            TechStackInUseException.class,
            () -> techStackService.delete(techStack.getId())
        );

        assertEquals(List.of(project.getId()), ex.getUsedByProjectIds());
        assertTrue(techStackRepository.findById(techStack.getId()).isPresent());
    }
}
