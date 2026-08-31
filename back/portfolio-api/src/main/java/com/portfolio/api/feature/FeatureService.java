package com.portfolio.api.feature;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.api.domain.Project;
import com.portfolio.api.domain.ProjectFeature;
import com.portfolio.api.exception.ResourceNotFoundException;
import com.portfolio.api.feature.dto.FeatureRequest;
import com.portfolio.api.project.dto.FeatureResponse;
import com.portfolio.api.repository.ProjectFeatureRepository;
import com.portfolio.api.repository.ProjectRepository;

@Service
@Transactional(readOnly = true)
public class FeatureService {

    private final ProjectFeatureRepository projectFeatureRepository;
    private final ProjectRepository projectRepository;

    public FeatureService(ProjectFeatureRepository projectFeatureRepository, ProjectRepository projectRepository) {
        this.projectFeatureRepository = projectFeatureRepository;
        this.projectRepository = projectRepository;
    }

    @Transactional
    public FeatureResponse create(Long projectId, FeatureRequest request) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ResourceNotFoundException("프로젝트를 찾을 수 없습니다."));

        ProjectFeature feature = new ProjectFeature(
            project, request.name(), request.description(), request.reason(),
            request.imageUrl(), request.sortOrder() == null ? 0 : request.sortOrder()
        );

        return FeatureResponse.from(projectFeatureRepository.save(feature));
    }

    @Transactional
    public FeatureResponse update(Long id, FeatureRequest request) {
        ProjectFeature feature = getOrThrow(id);
        feature.update(
            request.name(), request.description(), request.reason(),
            request.imageUrl(), request.sortOrder() == null ? feature.getSortOrder() : request.sortOrder()
        );
        return FeatureResponse.from(feature);
    }

    @Transactional
    public void delete(Long id) {
        projectFeatureRepository.delete(getOrThrow(id));
    }

    private ProjectFeature getOrThrow(Long id) {
        return projectFeatureRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("기능을 찾을 수 없습니다."));
    }
}
