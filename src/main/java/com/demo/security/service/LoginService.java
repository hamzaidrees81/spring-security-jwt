package com.demo.security.service;

import com.demo.security.dto.Credentials;
import com.demo.security.dto.Token;

public interface LoginService {

  public Token authenticate(Credentials credentials);
}
