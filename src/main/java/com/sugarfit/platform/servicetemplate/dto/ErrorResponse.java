package com.sugarfit.platform.servicetemplate.dto;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // skip null fields like fieldErrors when there are none
public class ErrorResponse {
    private String status;
    private String message;
    private String requestId;
    private Instant timestamp;
    private List<FieldError> fieldErrors;

    @Getter
    @Builder
    public static class FieldError {
        private String field;
        private String message;
    }
}
