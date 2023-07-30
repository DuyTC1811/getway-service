package com.example.gatewayservice.utils;

import io.exceptions.models.JwtTokenMalformedException;
import io.exceptions.models.JwtTokenMissingException;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {
    @Value("${jwt.jwtSecret}")
    private String jwtSecret;
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

    /**
     * Phương thức này dùng để check Token
     * jwtSecret: mã key dùng để đối chiếu
     *
     * @param authToken một chuỗi Token
     * @return true hoặc false
     */
    public void validateJwtToken(final String authToken) throws JwtTokenMissingException, JwtTokenMalformedException {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
        } catch (SignatureException exception) {
            LOGGER.error("Invalid JWT signature: {}", exception.getMessage());
            throw new JwtTokenMalformedException("Invalid JWT signature");
        } catch (MalformedJwtException exception) {
            LOGGER.error("Invalid JWT token: {}", exception.getMessage());
            throw new JwtTokenMalformedException("Invalid JWT token");
        } catch (ExpiredJwtException exception) {
            LOGGER.error("JWT token is expired: {}", exception.getMessage());
            throw new JwtTokenMalformedException("JWT token is expired");
        } catch (UnsupportedJwtException exception) {
            LOGGER.error("JWT token is unsupported: {}", exception.getMessage());
            throw new JwtTokenMalformedException("JWT token is unsupported");
        } catch (IllegalArgumentException exception) {
            LOGGER.error("JWT claims string is empty: {}", exception.getMessage());
            throw new JwtTokenMissingException("JWT claims string is empty");
        } catch (NoClassDefFoundError exception) {
            LOGGER.error("Invalid token: {}", exception.getMessage());
            throw new JwtTokenMissingException("Invalid token");
        }
    }
}
