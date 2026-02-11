package com.sugarfit.platform.servicetemplate.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sugarfit.platform.servicetemplate.dto.ExampleRequest;
import com.sugarfit.platform.servicetemplate.dto.ExampleResponse;
import com.sugarfit.platform.servicetemplate.service.ExampleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/example")
public class ExampleController {

    private final ExampleService exampleService;

    public ExampleController(ExampleService exampleService) {
        this.exampleService = exampleService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExampleResponse> processExample(@Valid @RequestBody ExampleRequest request) {
        return ResponseEntity.ok(exampleService.process(request));
    }
}
