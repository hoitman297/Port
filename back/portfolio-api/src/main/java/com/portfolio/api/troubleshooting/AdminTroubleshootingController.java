package com.portfolio.api.troubleshooting;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.api.project.dto.TroubleshootingResponse;
import com.portfolio.api.troubleshooting.dto.TroubleshootingRequest;

import jakarta.validation.Valid;

/** BO — requires authentication. */
@RestController
@RequestMapping("/api/admin")
public class AdminTroubleshootingController {

    private final TroubleshootingService troubleshootingService;

    public AdminTroubleshootingController(TroubleshootingService troubleshootingService) {
        this.troubleshootingService = troubleshootingService;
    }

    @PostMapping("/features/{featureId}/troubleshootings")
    public ResponseEntity<TroubleshootingResponse> createTroubleshooting(
        @PathVariable Long featureId,
        @Valid @RequestBody TroubleshootingRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(troubleshootingService.create(featureId, request));
    }

    @PutMapping("/troubleshootings/{id}")
    public TroubleshootingResponse updateTroubleshooting(
        @PathVariable Long id,
        @Valid @RequestBody TroubleshootingRequest request
    ) {
        return troubleshootingService.update(id, request);
    }

    @DeleteMapping("/troubleshootings/{id}")
    public ResponseEntity<Void> deleteTroubleshooting(@PathVariable Long id) {
        troubleshootingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
