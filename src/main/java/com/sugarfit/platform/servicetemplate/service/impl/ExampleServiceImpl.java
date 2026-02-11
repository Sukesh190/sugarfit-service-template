package com.sugarfit.platform.servicetemplate.service.impl;

import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import com.sugarfit.platform.servicetemplate.constants.AppConstants;
import com.sugarfit.platform.servicetemplate.dto.ExampleRequest;
import com.sugarfit.platform.servicetemplate.dto.ExampleResponse;
import com.sugarfit.platform.servicetemplate.service.ExampleService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExampleServiceImpl implements ExampleService {

    @Override
    public ExampleResponse process(ExampleRequest request) {

        String requestId = MDC.get(AppConstants.REQUEST_ID_MDC_KEY);
        log.info("Processing request for userId={} with value={}", request.getUserId(), request.getValue());
        return ExampleResponse.builder().status(AppConstants.STATUS_SUCCESS).requestId(requestId).build();
    }

}
