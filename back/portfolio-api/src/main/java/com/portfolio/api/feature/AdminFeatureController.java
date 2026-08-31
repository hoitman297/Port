package com.portfolio.api.feature;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.api.feature.dto.FeatureRequest;
import com.portfolio.api.project.dto.FeatureResponse;

import jakarta.validation.Valid;

/** BO — requires authentication. */
@RestController
@RequestMapping("/api/admin")
public class AdminFeatureController {

    private final FeatureService featureService;

    public AdminFeatureController(FeatureService featureService) {
        this.featureService = featureService;
    }

    @PostMapping("/projects/{projectId}/features")
    public ResponseEntity<FeatureResponse> createFeature(
        @PathVariable Long projectId,
        @Valid @RequestBody FeatureRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(featureService.create(projectId, request));
    }

    @PutMapping("/features/{id}")
    public FeatureResponse updateFeature(@PathVariable Long id, @Valid @RequestBody FeatureRequest request) {
        return featureService.update(id, request);
    }

    @DeleteMapping("/features/{id}")
    public ResponseEntity<Void> deleteFeature(@PathVariable Long id) {
        featureService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
