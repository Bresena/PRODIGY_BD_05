package com.hotel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity 
@Table(name="users")
@Data
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class User {
	  @Id 
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;

	  @NotBlank 
	  @Size(min=2,max=50)
	  private String name;

	  @NotBlank 
	  @Email @Column(unique = true)
	  private String email;

	  @NotBlank
	  private String password; // BCrypt

	  @Enumerated(EnumType.STRING)
	  private Role role = Role.USER;
}