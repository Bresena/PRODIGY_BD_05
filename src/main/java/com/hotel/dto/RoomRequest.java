package com.hotel.dto;

import jakarta.validation.constraints.*;

public record RoomRequest(
		@NotBlank 
		String title,
		
		@NotBlank 
		String description,
		  
	    @NotNull 
		@Positive 
		Double pricePerNight
) {}
