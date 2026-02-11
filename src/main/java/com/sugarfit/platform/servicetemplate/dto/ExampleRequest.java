package com.sugarfit.platform.servicetemplate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ExampleRequest {

    @NotBlank(message = "User ID is required and cannot be blank")
    private String  userId;

    @NotNull(message = "Value is required")
    private Integer value;
}


