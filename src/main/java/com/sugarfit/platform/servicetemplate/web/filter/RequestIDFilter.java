package com.sugarfit.platform.servicetemplate.web.filter;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sugarfit.platform.servicetemplate.constants.AppConstants;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // runs first so every downstream log line has the request ID
@Slf4j
public class RequestIDFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestID = UUID.randomUUID().toString();
        log.debug("Request id generated: {}", requestID);

        MDC.put(AppConstants.REQUEST_ID_MDC_KEY, requestID);

        try {
            filterChain.doFilter(request, response);
        } finally {
            // clean up to avoid leaking into the next request on the same thread
            MDC.remove(AppConstants.REQUEST_ID_MDC_KEY);
            log.debug("Request id removed: {}", requestID);
        }

    }

}
