package com.portfolio.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.portfolio.api.domain.ProjectFeature;

public interface ProjectFeatureRepository extends JpaRepository<ProjectFeature, Long> {

    @Query("""
        SELECT DISTINCT f FROM ProjectFeature f
        LEFT JOIN FETCH f.troubleshooting
        WHERE f.project.id = :projectId
        ORDER BY f.sortOrder ASC
        """)
    List<ProjectFeature> findWithTroubleshootingByProjectIdOrderBySortOrderAsc(Long projectId);
}
