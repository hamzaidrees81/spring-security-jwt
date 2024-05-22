package com.demo.security.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class JWTTokenConfig {

  @Value("${config.jwt.issuer}")
  private String issuer;

  @Value("${config.jwt.leeway}")
  private long leeway;

  @Value("${config.jwt.key}")
  private String jwtKey;

  @Bean //create a verifier bean
  public JWTVerifier getJWTVerifier(Algorithm algorithm) {
    return JWT.require(algorithm)
      .acceptLeeway(leeway) //time buffer after which token can still be considered valid
      .withIssuer(issuer).build();
  }

  @Bean //create a bean for algorithm
  public Algorithm getJWTAlgorithm() {
    return Algorithm.HMAC512(jwtKey);
  }
}
