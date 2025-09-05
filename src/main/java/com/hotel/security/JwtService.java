package com.hotel.security;

import java.security.Key;
import java.time.Instant;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	private final Key key;
	  private final long expirationMinutes;

	  public JwtService(@Value("${app.jwt.secret}") String secret,
	                    @Value("${app.jwt.expirationMinutes:120}") long expMin) {
	    this.key = Keys.hmacShaKeyFor(secret.getBytes()); // >= 256-bit
	    this.expirationMinutes = expMin;
	  }

	  public String generate(Map<String, Object> claims, String subject) {
	    Instant now = Instant.now();
	    return Jwts.builder()
	        .setClaims(claims)
	        .setSubject(subject)
	        .setIssuedAt(Date.from(now))
	        .setExpiration(Date.from(now.plusSeconds(expirationMinutes * 60)))
	        .signWith(key, SignatureAlgorithm.HS256)
	        .compact();
	  }

	  public String getSubject(String token) {
	    return Jwts.parserBuilder().setSigningKey(key).build()
	        .parseClaimsJws(token).getBody().getSubject();
	  }
}