package com.demo.security.controller;

import com.demo.security.dto.Credentials;
import com.demo.security.dto.Token;
import com.demo.security.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloWorldController {

  private final LoginService loginService;

  @GetMapping("/public")
  ResponseEntity<String> helloPublic() {
    return ResponseEntity.ok("Hello Public!");
  }

  @PostMapping("/login")
  Token login(@RequestBody Credentials credentials) {
    return loginService.authenticate(credentials);
  }

  @GetMapping("/configuration")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  ResponseEntity<String> helloConfig() {
    return ResponseEntity.ok("Hello Configuration!");
  }

  @GetMapping("/history")
  @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
  ResponseEntity<String> helloHistory() {
    return ResponseEntity.ok("Hello History!");
  }

  @GetMapping("/world")
  ResponseEntity<String> helloWorld() {
    return ResponseEntity.ok("Hello World!");
  }
}
