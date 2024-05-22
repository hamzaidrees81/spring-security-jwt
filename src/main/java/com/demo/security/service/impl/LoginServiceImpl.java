package com.demo.security.service.impl;

import com.demo.security.dto.Credentials;
import com.demo.security.dto.Token;
import com.demo.security.service.LoginService;
import com.demo.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService
{
  private final TokenService tokenService;

  public Token authenticate(Credentials credentials) {
    /*
    simulate a successful login by returning whatever role a user passes in the request
     */
    return tokenService.generateToken(credentials.getAuthority(), credentials.getUsername());
  }
}
