package com.portfolio.api.project.dto;

import java.util.List;

import com.portfolio.api.domain.Project;
import com.portfolio.api.techstack.dto.TechStackResponse;

/** Shared shape for GET /api/projects and GET /api/admin/projects — the spec doesn't call for a distinct admin shape. */
public record ProjectListItemResponse(
    Long id,
    String title,
    String summary,
    String thumbnailUrl,
    List<TechStackResponse> techStacks
) {

    public static ProjectListItemResponse from(Project project) {
        List<TechStackResponse> techStacks = project.getProjectTechStacks().stream()
            .map(pts -> TechStackResponse.from(pts.getTechStack()))
            .toList();

        return new ProjectListItemResponse(
            project.getId(), project.getTitle(), project.getSummary(), project.getThumbnailUrl(), techStacks
        );
    }
}
