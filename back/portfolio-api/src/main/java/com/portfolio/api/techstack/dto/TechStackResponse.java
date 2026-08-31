package com.portfolio.api.techstack.dto;

import com.portfolio.api.domain.TechStack;

public record TechStackResponse(Long id, String name, String category) {

    public static TechStackResponse from(TechStack techStack) {
        return new TechStackResponse(techStack.getId(), techStack.getName(), techStack.getCategory());
    }
}
