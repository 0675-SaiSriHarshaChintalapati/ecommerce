package com.digit.ecommerce.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.digit.ecommerce.dto.DataHolder;

public class TokenUtility {
    private final static String SECRET = "1234567890zxcvbnmasdfghjklqwertyuiop!@#$%^&*()";
    public String getToken(Long id, String role) {
        return JWT.create()
                .withClaim("id", id)
                .withClaim("role", role)
                .sign(Algorithm.HMAC256(SECRET));
    }
    public DataHolder decode(String token) {
        Long id = JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token).getClaim("id").asLong();
        String role = JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token).getClaim("role").asString();
        return DataHolder.builder().id(id).role(role).build();
    }
}