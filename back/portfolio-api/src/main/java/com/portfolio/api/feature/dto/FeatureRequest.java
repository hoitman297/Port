package com.portfolio.api.feature.dto;

import jakarta.validation.constraints.NotBlank;

public record FeatureRequest(
    @NotBlank(message = "기능명을 입력해주세요.") String name,
    String imageUrl,
    String description,
    String reason,
    Integer sortOrder
) {
}
