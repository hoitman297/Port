package com.portfolio.api.techstack;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.api.techstack.dto.TechStackRequest;
import com.portfolio.api.techstack.dto.TechStackResponse;

import jakarta.validation.Valid;

/** BO — requires authentication. */
@RestController
@RequestMapping("/api/admin/tech-stacks")
public class AdminTechStackController {

    private final TechStackService techStackService;

    public AdminTechStackController(TechStackService techStackService) {
        this.techStackService = techStackService;
    }

    @GetMapping
    public List<TechStackResponse> getTechStacks() {
        return techStackService.getAll();
    }

    @PostMapping
    public ResponseEntity<TechStackResponse> createTechStack(@Valid @RequestBody TechStackRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(techStackService.create(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechStack(@PathVariable Long id) {
        techStackService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
