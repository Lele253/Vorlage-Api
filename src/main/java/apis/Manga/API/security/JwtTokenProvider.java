package apis.Manga.API.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;




@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    public String generateToken(String userEmail){
        Instant now = Instant.now();
        Instant expiration = now.plus(1, ChronoUnit.DAYS);
        return Jwts.builder()
                .setSubject(userEmail)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String generateToken(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return generateToken(user.getUsername());
    }

    public String getUserMailFromToken(String token){
        Jws<Claims> jwsClaims = Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token);

        Claims claims = jwsClaims.getBody();
        return claims.getSubject();
    }

    public boolean valideToken(String token) {
        try {
            Jws<Claims> jwsClaims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token);

            // Das Token wurde erfolgreich validiert, du kannst hier weitere Verarbeitungsschritte durchführen
            return true;
        } catch (JwtException ex) {
            if (ex instanceof SignatureException) {
                log.error("Exception1");
            } else if (ex instanceof MalformedJwtException) {
                log.error("Exception2");
            } else if (ex instanceof ExpiredJwtException) {
                log.error("Exceptionxxx");
            } else if (ex instanceof UnsupportedJwtException) {
                log.error("Exception4");
            } else {
                log.error("Unexpected exception: " + ex.getMessage());
            }

            return false;
        }
    }
}
