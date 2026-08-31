package com.portfolio.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.portfolio.api.domain.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    /**
     * Single collection fetch join (projectTechStacks -> techStack) is safe from
     * MultipleBagFetchException. Used for both the FO and BO list endpoints.
     */
    @Query("""
        SELECT DISTINCT p FROM Project p
        LEFT JOIN FETCH p.projectTechStacks pts
        LEFT JOIN FETCH pts.techStack
        ORDER BY p.id DESC
        """)
    List<Project> findAllWithTechStacks();

    /**
     * Detail fetch fetches only the project + its tech stacks. Features (and their
     * troubleshooting) are loaded separately via
     * {@link com.portfolio.api.repository.ProjectFeatureRepository#findWithTroubleshootingByProjectIdOrderBySortOrderAsc}
     * to avoid a MultipleBagFetchException from fetching two collections at once.
     */
    @Query("""
        SELECT DISTINCT p FROM Project p
        LEFT JOIN FETCH p.projectTechStacks pts
        LEFT JOIN FETCH pts.techStack
        WHERE p.id = :id
        """)
    Optional<Project> findDetailById(Long id);
}
