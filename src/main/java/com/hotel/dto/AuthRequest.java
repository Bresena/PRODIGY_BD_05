package com.hotel.dto;

import jakarta.validation.constraints.*;

public record AuthRequest(
		@NotBlank 
		@Email 
		String email,
		
		@NotBlank 
		String password,
		
		@NotBlank 
		String name // used for register
) {}
