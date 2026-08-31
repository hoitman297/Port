package com.portfolio.api.project.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.portfolio.api.domain.Project;
import com.portfolio.api.domain.ProjectFeature;
import com.portfolio.api.domain.Troubleshooting;

/**
 * Core rule from the DB design: troubleshooting is 1:0..1 off of PROJECT_FEATURE, and the
 * detail response must surface that as a null field (not an empty object) so FO can skip
 * rendering the troubleshooting card for features that never had one.
 */
class FeatureResponseTest {

    @Test
    void troubleshootingIsNullWhenFeatureHasNone() {
        Project project = new Project("제목", "요약", null, null, null, null, null);
        ProjectFeature feature = new ProjectFeature(project, "다크모드 지원", "설명", "이유", null, 1);

        FeatureResponse response = FeatureResponse.from(feature);

        assertNull(response.troubleshooting());
    }

    @Test
    void troubleshootingIsPopulatedWhenFeatureHasOne() {
        Project project = new Project("제목", "요약", null, null, null, null, null);
        ProjectFeature feature = new ProjectFeature(project, "실시간 알림", "설명", "이유", null, 1);
        Troubleshooting troubleshooting = new Troubleshooting(feature, "문제", "분석", "실행", "결과");
        feature.setTroubleshooting(troubleshooting);

        FeatureResponse response = FeatureResponse.from(feature);

        assertNotNull(response.troubleshooting());
        assertEquals("문제", response.troubleshooting().problem());
        assertEquals("분석", response.troubleshooting().analysis());
        assertEquals("실행", response.troubleshooting().action());
        assertEquals("결과", response.troubleshooting().result());
    }
}
