package com.demo.security.dto;

import lombok.Data;

@Data
public class Credentials {
  String username;
  String password;
  UserRole authority;
}
