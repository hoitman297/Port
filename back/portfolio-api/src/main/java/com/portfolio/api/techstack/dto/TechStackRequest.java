package com.portfolio.api.techstack.dto;

import jakarta.validation.constraints.NotBlank;

public record TechStackRequest(
    @NotBlank(message = "이름을 입력해주세요.") String name,
    @NotBlank(message = "카테고리를 입력해주세요.") String category
) {
}
