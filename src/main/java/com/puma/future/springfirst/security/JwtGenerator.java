package com.puma.future.springfirst.security;


import io.jsonwebtoken.*;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtGenerator {

    // Генерируем ЖВТ токен

    public String generateToken(Authentication authentication){
        // Получаем имя пользователя, которые аутентифицируется
        String username = authentication.getName();
        Date currentDate = new Date(); // Дата создания токена
        Date expiredDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION); // Дата когда истечет его срок "годности"
        // Создаем токен
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.JWT_SECRET)
                .compact();

        return token;
    }

    // Получаем из жвт токена имя пользователя
    public String getUsernameFromJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(SecurityConstants.JWT_SECRET).parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect");
        }
    }


}
