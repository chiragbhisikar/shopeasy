package com.chiragbhisikar.shopease.Auth.Config;

import com.chiragbhisikar.shopease.Model.Auth.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JWTTokenHelper {
    private static final String SECRET = "36763979244226452948404D635166546A576D5A7134743777217A25432A462D";
    private static final Key secretKey = Keys.hmacShaKeyFor(SECRET.getBytes());

    //    Generate Token Here
    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        String token = this.generateToken(user);

        return token;
    }

    public String generateToken(User user) {
        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        //  Collecting Data
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getUsername());
        claims.put("role", roles);

        Date currentDate = new Date();
        Date expireDate = new Date(System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000));

        String token = Jwts.builder().setClaims(claims) // Set custom claims
                .setIssuedAt(currentDate).setExpiration(expireDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();

        return token;
    }


    public String getUserNameFromToken(String authToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(authToken)
                .getBody();

        return (String) claims.get("email");
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(authToken);

            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect", ex.fillInStackTrace());
        }
    }

}
