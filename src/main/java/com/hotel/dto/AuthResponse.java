package com.hotel.dto;

public record AuthResponse(
		String token,
		
		String role, 
		
		Long userId
) {}