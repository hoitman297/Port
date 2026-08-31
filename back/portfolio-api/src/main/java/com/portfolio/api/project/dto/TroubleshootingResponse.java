package com.portfolio.api.project.dto;

import com.portfolio.api.domain.Troubleshooting;

/** id is extra beyond the FO spec example, but BO needs it to PUT/DELETE a specific troubleshooting later. */
public record TroubleshootingResponse(Long id, String problem, String analysis, String action, String result) {

    public static TroubleshootingResponse from(Troubleshooting troubleshooting) {
        return new TroubleshootingResponse(
            troubleshooting.getId(),
            troubleshooting.getProblem(),
            troubleshooting.getAnalysis(),
            troubleshooting.getAction(),
            troubleshooting.getResult()
        );
    }
}
