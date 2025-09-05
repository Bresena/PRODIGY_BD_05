package com.hotel.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(indexes = {
		  @Index(name="idx_booking_room", columnList="room_id"),
		  @Index(name="idx_booking_guest", columnList="guest_id")})
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Booking {
	  @Id 
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;

	  @ManyToOne(optional=false, fetch=FetchType.LAZY)
	  private Room room;

	  @ManyToOne(optional=false, fetch=FetchType.LAZY)
	  private User guest;

	  @NotNull 
	  private LocalDate checkIn;
	  
	  @NotNull 
	  private LocalDate checkOut; // exclusive
	}
