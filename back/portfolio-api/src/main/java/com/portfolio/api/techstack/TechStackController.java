package com.portfolio.api.techstack;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.api.techstack.dto.TechStackResponse;

/** FO — public, unauthenticated. */
@RestController
@RequestMapping("/api/tech-stacks")
public class TechStackController {

    private final TechStackService techStackService;

    public TechStackController(TechStackService techStackService) {
        this.techStackService = techStackService;
    }

    @GetMapping
    public List<TechStackResponse> getTechStacks() {
        return techStackService.getAll();
    }
}
