package com.sugarfit.platform.servicetemplate.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExampleResponse {
    private String status;
    private String requestId;
}
