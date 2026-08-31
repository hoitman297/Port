package com.portfolio.api.project;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.api.domain.Project;
import com.portfolio.api.domain.ProjectFeature;
import com.portfolio.api.domain.ProjectTechStack;
import com.portfolio.api.domain.TechStack;
import com.portfolio.api.exception.ResourceNotFoundException;
import com.portfolio.api.project.dto.FeatureResponse;
import com.portfolio.api.project.dto.ProjectDetailResponse;
import com.portfolio.api.project.dto.ProjectListItemResponse;
import com.portfolio.api.project.dto.ProjectRequest;
import com.portfolio.api.repository.ProjectFeatureRepository;
import com.portfolio.api.repository.ProjectRepository;
import com.portfolio.api.repository.TechStackRepository;
import com.portfolio.api.techstack.dto.TechStackResponse;

@Service
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectFeatureRepository projectFeatureRepository;
    private final TechStackRepository techStackRepository;

    public ProjectService(
        ProjectRepository projectRepository,
        ProjectFeatureRepository projectFeatureRepository,
        TechStackRepository techStackRepository
    ) {
        this.projectRepository = projectRepository;
        this.projectFeatureRepository = projectFeatureRepository;
        this.techStackRepository = techStackRepository;
    }

    public List<ProjectListItemResponse> getList() {
        return projectRepository.findAllWithTechStacks().stream()
            .map(ProjectListItemResponse::from)
            .toList();
    }

    public ProjectDetailResponse getDetail(Long id) {
        Project project = projectRepository.findDetailById(id)
            .orElseThrow(() -> new ResourceNotFoundException("프로젝트를 찾을 수 없습니다."));

        List<TechStackResponse> techStacks = project.getProjectTechStacks().stream()
            .map(pts -> TechStackResponse.from(pts.getTechStack()))
            .toList();

        List<FeatureResponse> features = projectFeatureRepository
            .findWithTroubleshootingByProjectIdOrderBySortOrderAsc(id).stream()
            .map(FeatureResponse::from)
            .toList();

        return new ProjectDetailResponse(
            project.getId(), project.getTitle(), project.getSummary(), project.getThumbnailUrl(),
            project.getGithubUrl(), project.getDemoUrl(), project.getStartDate(), project.getEndDate(),
            techStacks, features
        );
    }

    @Transactional
    public ProjectListItemResponse create(ProjectRequest request) {
        Project project = new Project(
            request.title(), request.summary(), request.thumbnailUrl(),
            request.githubUrl(), request.demoUrl(), request.startDate(), request.endDate()
        );
        attachTechStacks(project, request.techStackIdsOrEmpty());
        return ProjectListItemResponse.from(projectRepository.save(project));
    }

    @Transactional
    public ProjectListItemResponse update(Long id, ProjectRequest request) {
        Project project = projectRepository.findDetailById(id)
            .orElseThrow(() -> new ResourceNotFoundException("프로젝트를 찾을 수 없습니다."));

        project.update(
            request.title(), request.summary(), request.thumbnailUrl(),
            request.githubUrl(), request.demoUrl(), request.startDate(), request.endDate()
        );

        project.getProjectTechStacks().clear();
        attachTechStacks(project, request.techStackIdsOrEmpty());

        return ProjectListItemResponse.from(project);
    }

    @Transactional
    public void delete(Long id) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("프로젝트를 찾을 수 없습니다."));
        projectRepository.delete(project);
    }

    private void attachTechStacks(Project project, List<Long> techStackIds) {
        for (Long techStackId : techStackIds) {
            TechStack techStack = techStackRepository.findById(techStackId)
                .orElseThrow(() -> new ResourceNotFoundException("기술 스택을 찾을 수 없습니다."));
            project.getProjectTechStacks().add(new ProjectTechStack(project, techStack));
        }
    }
}
