package com.sugarfit.platform.servicetemplate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sugarfit.platform.servicetemplate.IntegrationTest;

@IntegrationTest
public class ExampleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST /example with valid body should return 200 SUCCESS")
    void postExample_withValidBody_shouldReturnSuccess() throws Exception {
        String requestBody = "{\"userId\": \"123\",\"value\": 42}";

        mockMvc.perform(post("/example")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.requestId").exists());
    }

    @Test
    @DisplayName("POST /example with missing value should return 400")
    void postExample_withMissingValue_shouldReturnBadRequest() throws Exception {
        String requestBody = """
                {
                    "userId": "123"
                }
                """;

        mockMvc.perform(post("/example")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.fieldErrors").isArray());
    }

    @Test
    @DisplayName("POST /example with blank userId should return 400")
    void postExample_withBlankUserId_shouldReturnBadRequest() throws Exception {
        String requestBody = """
                {
                    "userId": "",
                    "value": 42
                }
                """;

        mockMvc.perform(post("/example")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()));
    }

    @Test
    @DisplayName("GET /example should return 405 Method Not Allowed")
    void getExample_shouldReturnMethodNotAllowed() throws Exception {
        mockMvc.perform(get("/example"))
                .andExpect(status().isMethodNotAllowed());
    }

}
