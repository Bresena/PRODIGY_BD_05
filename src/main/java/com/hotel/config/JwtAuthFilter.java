package com.hotel.config;

import com.hotel.security.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class JwtAuthFilter extends GenericFilter {
  private final JwtService jwt;
  private final CustomUserDetailsService uds;

  public JwtAuthFilter(JwtService jwt, CustomUserDetailsService uds) {
    this.jwt = jwt; this.uds = uds;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    String auth = req.getHeader("Authorization");
    if (auth != null && auth.startsWith("Bearer ")) {
      String token = auth.substring(7);
      try {
        String email = jwt.getSubject(token);
        var userDetails = uds.loadUserByUsername(email);
        var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      } catch (Exception ignored) { /* will fall through as anonymous */ }
    }
    chain.doFilter(request, response);
  }
}