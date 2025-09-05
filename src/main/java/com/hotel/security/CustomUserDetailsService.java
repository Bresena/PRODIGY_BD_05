package com.hotel.security;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.hotel.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	private final UserRepository users;

	  public CustomUserDetailsService(UserRepository users) { this.users = users; }

	  @Override
	  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	    var u = users.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
	    return org.springframework.security.core.userdetails.User.withUsername(u.getEmail())
	        .password(u.getPassword())
	        .authorities("ROLE_" + u.getRole().name())
	        .build();
	  }
}