package com.sushma.jobtracker.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationMs;

    // ===================== TOKEN GENERATION =====================

    /** Build a JWT for a user (no extra claims). */
    public String generateToken(UserDetails user) {
        return generateToken(new HashMap<>(), user);
    }

    /** Build a JWT with custom claims (e.g., role, name). */
    public String generateToken(Map<String, Object> extraClaims, UserDetails user) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(user.getUsername())                                  // 'sub' claim = email
                .issuedAt(new Date(System.currentTimeMillis()))               // 'iat'
                .expiration(new Date(System.currentTimeMillis() + expirationMs))  // 'exp'
                .signWith(getSigningKey())                                    // sign with secret
                .compact();
    }

    // ===================== TOKEN VALIDATION =====================

    /** Get the email (subject) out of a token. */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /** Is this token valid for the given user (right user + not expired)? */
    public boolean isTokenValid(String token, UserDetails user) {
        String emailInToken = extractUsername(token);
        return emailInToken.equals(user.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    // ===================== INTERNAL HELPERS =====================

    /** Generic claim extractor — pass it a getter like Claims::getSubject. */
    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}