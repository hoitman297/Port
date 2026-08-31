package com.portfolio.api.project.dto;

import java.time.LocalDate;
import java.util.List;

import com.portfolio.api.techstack.dto.TechStackResponse;

public record ProjectDetailResponse(
    Long id,
    String title,
    String summary,
    String thumbnailUrl,
    String githubUrl,
    String demoUrl,
    LocalDate startDate,
    LocalDate endDate,
    List<TechStackResponse> techStacks,
    List<FeatureResponse> features
) {
}
