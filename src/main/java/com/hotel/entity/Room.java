package com.hotel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Room {
	  @Id 
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;

	  @NotBlank 
	  private String title;
	  
	  @NotBlank 
	  private String description;

	  @Positive 
	  @NotNull 
	  private Double pricePerNight;

	  @ManyToOne(optional = false, fetch = FetchType.LAZY)
	  private User owner; // the lister/host
}
