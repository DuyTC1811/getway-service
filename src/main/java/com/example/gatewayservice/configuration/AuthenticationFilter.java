package com.example.gatewayservice.configuration;

import com.example.gatewayservice.utils.JwtUtils;
import io.exceptions.models.JwtTokenMalformedException;
import io.exceptions.models.JwtTokenMissingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

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
                String token = parseJwt(exchange);
                if (token == null) {
                    LOGGER.error("Missing authorization information!");
                    return buildErrorResponse(exchange, HttpStatus.UNAUTHORIZED);
                }

                try {
                    jwtUtils.validateJwtToken(token);
                } catch (JwtTokenMissingException e) {
                    return buildErrorResponse(exchange, HttpStatus.FORBIDDEN);
                } catch (JwtTokenMalformedException e) {
                    return buildErrorResponse(exchange, HttpStatus.UNAUTHORIZED);
                }

            return chain.filter(exchange);
        };
    }

    public static class Config {
        // Put the configuration properties
    }
    private Mono<Void> buildErrorResponse(ServerWebExchange exchange, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        return response.setComplete();
    }

    private String parseJwt(ServerWebExchange exchange) {
        String headerAuth = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
