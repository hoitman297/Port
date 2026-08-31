package com.portfolio.api.exception;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(String message, Map<String, String> errors, java.util.List<Long> usedByProjectIds) {

    public static ErrorResponse of(String message) {
        return new ErrorResponse(message, null, null);
    }

    public static ErrorResponse validation(String message, Map<String, String> errors) {
        return new ErrorResponse(message, errors, null);
    }

    public static ErrorResponse techStackInUse(String message, java.util.List<Long> usedByProjectIds) {
        return new ErrorResponse(message, null, usedByProjectIds);
    }
}
