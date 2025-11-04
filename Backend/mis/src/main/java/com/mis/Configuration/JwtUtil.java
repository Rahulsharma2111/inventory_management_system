package com.mis.Configuration;

import com.mis.Entity.Organisation;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private String SECRET_KEY="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Organisation organisation) {
        return Jwts.builder()
                .subject(organisation.getEmail())
                .claim("organisationId", organisation.getId().toString())
                .claim("name", organisation.getName())
                .claim("registrationNumber", organisation.getRegistrationNumber())
                .claim("type", organisation.getType())
                .claim("email", organisation.getEmail())
                .claim("phone", organisation.getPhone())
                .claim("website", organisation.getWebsite())
                .claim("gstNumber", organisation.getGstNumber())
                .claim("address", organisation.getAddress())
                .claim("city", organisation.getCity())
                .claim("state", organisation.getState())
                .claim("country", organisation.getCountry())
                .claim("pincode", organisation.getPincode())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(getSecretKey())
                .compact();
    }

//    public String generateToken(Organisation organisation){
//        return Jwts.builder()
//                .subject(organisation.getEmail())
//                .claim("organisationId",organisation.getId().toString())
//                .issuedAt(new Date())
//                .expiration(new Date(System.currentTimeMillis()+1000*60*10))
//                .signWith(getSecretKey())
//                .compact();
//    }

    public String getOrganisationFromToken(String token) {
        Claims claims= Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }
}
