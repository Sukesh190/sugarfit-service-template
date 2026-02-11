package com.sugarfit.platform.servicetemplate.service;

import com.sugarfit.platform.servicetemplate.dto.ExampleRequest;
import com.sugarfit.platform.servicetemplate.dto.ExampleResponse;

public interface ExampleService {

    ExampleResponse process(ExampleRequest request);

}
