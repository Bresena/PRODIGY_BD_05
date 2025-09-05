package com.hotel.config;

import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
	private final JwtAuthFilter jwtFilter;

	  public SecurityConfig(JwtAuthFilter jwtFilter) { this.jwtFilter = jwtFilter; }

	  @Bean
	  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http.csrf(csrf -> csrf.disable())
	       .authorizeHttpRequests(auth -> auth
	         .requestMatchers("/api/auth/**").permitAll()
	         .anyRequest().authenticated()
	       )
	       .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	    return http.build();
	  }

	  @Bean PasswordEncoder passwordEncoder() { 
		  return new BCryptPasswordEncoder(); 
		  }

	  @Bean AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
	    return cfg.getAuthenticationManager();
	  }
}
