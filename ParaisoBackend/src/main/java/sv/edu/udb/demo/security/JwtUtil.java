package sv.edu.udb.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET = "marioesbienhuevon_marioesbienhuevon1234";
    private final long EXPIRATION = 4000*120*120;


    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role",role)
                .setIssuedAt(new Date())
                .setExpiration(new Date((System.currentTimeMillis()+EXPIRATION)))
                .signWith(getKey())
                .compact();
    }

    public Claims extrClaims(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token).
                getBody();
    }

    public boolean isTokenValid(String token) {
        try {
            extrClaims(token);
            return true;
        }catch (JwtException e){
            return false;
        }
    }

    public String extractUsername(String token) {
        return extrClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extrClaims(token).get("role",String.class);
    }
}
