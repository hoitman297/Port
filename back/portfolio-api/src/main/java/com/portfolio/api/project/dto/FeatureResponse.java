package com.portfolio.api.project.dto;

import com.portfolio.api.domain.ProjectFeature;

public record FeatureResponse(
    Long id,
    String name,
    String imageUrl,
    String description,
    String reason,
    TroubleshootingResponse troubleshooting
) {

    /** troubleshooting is null whenever the feature has none — FO uses that to skip the card. */
    public static FeatureResponse from(ProjectFeature feature) {
        TroubleshootingResponse troubleshooting = feature.getTroubleshooting() == null
            ? null
            : TroubleshootingResponse.from(feature.getTroubleshooting());

        return new FeatureResponse(
            feature.getId(),
            feature.getName(),
            feature.getImageUrl(),
            feature.getDescription(),
            feature.getReason(),
            troubleshooting
        );
    }
}
