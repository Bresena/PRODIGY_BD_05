package com.hotel.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record BookingRequest(
		@NotNull 
		Long roomId,
		
		@NotNull 
		LocalDate checkIn,
		
	    @NotNull 
	    LocalDate checkOut
) {}
