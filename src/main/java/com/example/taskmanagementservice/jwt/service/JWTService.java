package com.example.taskmanagementservice.jwt.service;

import com.example.taskmanagementservice.exception.NotFoundException;
import com.example.taskmanagementservice.exception.TokenExpiredException;
import com.example.taskmanagementservice.jwt.model.Token;
import com.example.taskmanagementservice.jwt.repository.JWTRepository;
import com.example.taskmanagementservice.user.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JWTService {

    @Value("${jwt.access.secret-key}")
    private String accessSecretKey;

    @Value("${jwt.access.duration}")
    private long accessTokenDuration;

    @Value("${jwt.refresh.duration}")
    private long refreshTokenDuration;

    //private final static long refreshExpiration = 1000 * 60 * 60 * 24 * 3L;

    public String generateAccessToken(
            String username,
            Map<String, Object> claims
    ) {


    }

    public String generateRefreshToken(
            String username,
            Map<String, Object> claims
    ) {
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .and()
                .signWith(getKey())
                .compact();

    }

    private SecretKey getAccessKey() {
        byte[] keyBytes = Decoders.BASE64.decode(accessSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getAccessKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateAccessToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);

        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date(System.currentTimeMillis()));
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}