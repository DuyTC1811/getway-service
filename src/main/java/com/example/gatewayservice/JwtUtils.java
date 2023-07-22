package com.example.gatewayservice;

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
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException exception) {
            LOGGER.error("Invalid JWT signature: {}", exception.getMessage());
        } catch (MalformedJwtException exception) {
            LOGGER.error("Invalid JWT token: {}", exception.getMessage());
        } catch (ExpiredJwtException exception) {
            LOGGER.error("JWT token is expired: {}", exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            LOGGER.error("JWT token is unsupported: {}", exception.getMessage());
        } catch (IllegalArgumentException exception) {
            LOGGER.error("JWT claims string is empty: {}", exception.getMessage());
        }
        return false;
    }
}
