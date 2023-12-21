package com.puma.future.springfirst.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtGenerator tokenGenerator;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Извлекаем жвт из запроса
        String token = getJwtFromRequest(request);
        // Проверяем, что токен не пустой и проходит валидацию
        if (StringUtils.hasText(token) && tokenGenerator.validateToken(token)){
            // Извлекаем имя токена
            String username = tokenGenerator.getUsernameFromJWT(token);
            // Загружаем данные о юзере в UserDetails
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            // Создаем объект класса, в который предоставляем данные о пользователе
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                    null,
                    userDetails.getAuthorities());
            // Детали аутентификации устанавливаются с помощью метода setDetails, который получает информацию о запросе.
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // Аутентификационные данные устанавливаются в контекст безопасности с помощью метода setAuthentication объекта SecurityContextHolder.
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }

    // Метод getJwtFromRequest извлекает токен из заголовка запроса, проверяет его наличие и формат, и возвращает его значение без префикса "Bearer".
    private String getJwtFromRequest(HttpServletRequest request){
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")){
            return bearer.substring(7);
        }
        return null;
    }

    // Таким образом, данный код выполняет проверку и аутентификацию пользователя на основе переданного токена JWT.
}
