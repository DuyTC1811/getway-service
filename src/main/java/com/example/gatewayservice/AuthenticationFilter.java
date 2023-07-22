package com.example.gatewayservice;

import io.exceptions.models.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);
    private final JwtUtils jwtUtils;

    public AuthenticationFilter(JwtUtils jwtUtils) {
        super(Config.class);
        this.jwtUtils = jwtUtils;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (token == null) {
                LOGGER.error("Missing authorization information! ");
                throw new BaseException("Missing authorization information");
            }
            if (!jwtUtils.validateJwtToken(token)) {
                exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
        // Put the configuration properties
    }
}
