package com.portfolio.api.troubleshooting.dto;

import jakarta.validation.constraints.NotBlank;

public record TroubleshootingRequest(
    @NotBlank(message = "문제 상황을 입력해주세요.") String problem,
    @NotBlank(message = "원인 분석을 입력해주세요.") String analysis,
    @NotBlank(message = "해결 과정을 입력해주세요.") String action,
    @NotBlank(message = "결과를 입력해주세요.") String result
) {
}
