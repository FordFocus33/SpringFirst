package com.puma.future.springfirst.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private JwtAuthEntryPoint authEntryPoint;
    private CustomUserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthEntryPoint authEntryPoint) {
        this.authEntryPoint = authEntryPoint;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
       return http
               .authorizeHttpRequests(auth -> {
                   auth.requestMatchers("/api/auth/**", "/swagger-ui/**", "/api-docs/**").permitAll();
                   auth.anyRequest().authenticated();
               })
               .csrf(AbstractHttpConfigurer::disable)
               .exceptionHandling(exception -> {
                   exception.authenticationEntryPoint(authEntryPoint);
               })
               .sessionManagement(session -> {
                   session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
               })
               .httpBasic(Customizer.withDefaults())
               .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
               .build();
   }


   @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
   }

   @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
   }

   @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter();
   }
}
