package com.demo.security.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.demo.security.dto.Token;
import com.demo.security.dto.UserRole;
import com.demo.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

  private final Algorithm algorithm;
  @Value("${config.jwt.issuer}")
  private String issuer;

  @Value("${config.jwt.expirymilliseconds}")
  private long expiryOffset;

  @Override
  public Token generateToken(UserRole role, String userID) {

    String jwtToken = JWT.create()
      .withJWTId(UUID.randomUUID().toString())
      .withSubject("security-demo-token")
      .withIssuer(issuer)
      .withAudience("security-demo-audience")
      .withClaim("roles", List.of(role.toString()))
      .withClaim("userID", userID)
      .withExpiresAt(new Date().toInstant().plusMillis(expiryOffset))
      .sign(algorithm); //sign the token

    return Token.builder()
      .accessToken(jwtToken)
      .build();
  }
}
