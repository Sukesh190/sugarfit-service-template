package com.sugarfit.platform.servicetemplate.service.impl;

import org.slf4j.MDC;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.sugarfit.platform.servicetemplate.constants.AppConstants;
import com.sugarfit.platform.servicetemplate.dto.ExampleRequest;
import com.sugarfit.platform.servicetemplate.dto.ExampleResponse;

public class ExampleServiceImplTest {

    private ExampleServiceImpl exampleService;

    @BeforeEach
    void setUp() {
        exampleService = new ExampleServiceImpl();
        MDC.put(AppConstants.REQUEST_ID_MDC_KEY, "7e061fe3-e88b-4acf-a249-89b2bbea4e81");
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    @DisplayName("process() should return SUCCESS status")
    void process_shouldReturnSuccessStatus() {
        ExampleRequest request = new ExampleRequest();
        request.setUserId("123");
        request.setValue(42);

        ExampleResponse response = exampleService.process(request);

        assertEquals(AppConstants.STATUS_SUCCESS, response.getStatus());
    }

    @Test
    @DisplayName("process() should return the request ID from MDC")
    void process_shouldReturnRequestIdFromMdc() {
        ExampleRequest request = new ExampleRequest();
        request.setUserId("123");
        request.setValue(42);

        ExampleResponse response = exampleService.process(request);
        assertEquals("7e061fe3-e88b-4acf-a249-89b2bbea4e81", response.getRequestId());
    }

}
