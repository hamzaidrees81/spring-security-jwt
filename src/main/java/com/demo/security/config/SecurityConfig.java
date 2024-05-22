package com.demo.security.config;

import com.demo.security.filter.JWTTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JWTTokenFilter jwtTokenFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
      // Enable CORS and disable CSRF
      .cors(AbstractHttpConfigurer::disable)
      .csrf(AbstractHttpConfigurer::disable)
      .sessionManagement(AbstractHttpConfigurer::disable)
      // Setup authorization
      .authorizeHttpRequests((auth) -> {
        auth
          .requestMatchers("/public/**").permitAll()
          .requestMatchers("/login").permitAll()
          .anyRequest()
          .authenticated();
      })
      .exceptionHandling((exceptionHandling) -> exceptionHandling
        .accessDeniedPage("/error") // Redirect to /error page on access denied
      ).addFilterBefore(
        this.jwtTokenFilter,      //add filter
        UsernamePasswordAuthenticationFilter.class
      ).build();
  }
}