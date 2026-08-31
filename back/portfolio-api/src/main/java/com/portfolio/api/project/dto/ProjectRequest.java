package com.portfolio.api.project.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record ProjectRequest(
    @NotBlank(message = "제목을 입력해주세요.") String title,
    String summary,
    String thumbnailUrl,
    String githubUrl,
    String demoUrl,
    LocalDate startDate,
    LocalDate endDate,
    List<Long> techStackIds
) {

    public List<Long> techStackIdsOrEmpty() {
        return techStackIds == null ? List.of() : techStackIds;
    }
}
