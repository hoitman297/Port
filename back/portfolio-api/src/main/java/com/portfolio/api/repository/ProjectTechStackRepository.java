package com.portfolio.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.portfolio.api.domain.ProjectTechStack;

public interface ProjectTechStackRepository extends JpaRepository<ProjectTechStack, Long> {

    boolean existsByTechStackId(Long techStackId);

    @Query("SELECT DISTINCT pts.project.id FROM ProjectTechStack pts WHERE pts.techStack.id = :techStackId")
    List<Long> findProjectIdsByTechStackId(Long techStackId);
}
