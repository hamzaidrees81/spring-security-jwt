package com.demo.security.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Token {
  public String accessToken;
}
