package com.demo.security.service;

import com.demo.security.dto.Token;
import com.demo.security.dto.UserRole;
import org.springframework.stereotype.Component;

public interface TokenService {
  public Token generateToken(UserRole role, String userID);
}
