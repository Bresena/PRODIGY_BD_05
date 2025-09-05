package com.hotel.controller;

import com.hotel.dto.*;
import com.hotel.entity.*;
import com.hotel.repository.UserRepository;
import com.hotel.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController @RequestMapping("/api/auth")
public class AuthController {
  private final UserRepository users;
  private final PasswordEncoder encoder;
  private final AuthenticationManager authManager;
  private final JwtService jwt;

  public AuthController(UserRepository users, PasswordEncoder encoder, AuthenticationManager authManager, JwtService jwt) {
    this.users = users; this.encoder = encoder; this.authManager = authManager; this.jwt = jwt;
  }

  @PostMapping("/register")
  public AuthResponse register(@Valid @RequestBody AuthRequest req) {
    if (users.existsByEmail(req.email())) throw new IllegalArgumentException("Email already used");
    var u = User.builder()
        .name(req.name())
        .email(req.email())
        .password(encoder.encode(req.password()))
        .role(Role.USER)
        .build();
    users.save(u);
    String token = jwt.generate(Map.of("role", u.getRole().name()), u.getEmail());
    return new AuthResponse(token, u.getRole().name(), u.getId());
  }

  @PostMapping("/login")
  public AuthResponse login(@Valid @RequestBody AuthRequest req) {
    var auth = new UsernamePasswordAuthenticationToken(req.email(), req.password());
    authManager.authenticate(auth);
    var u = users.findByEmail(req.email()).orElseThrow();
    String token = jwt.generate(Map.of("role", u.getRole().name()), u.getEmail());
    return new AuthResponse(token, u.getRole().name(), u.getId());
  }
}