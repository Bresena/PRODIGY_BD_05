package com.hotel.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.*;

import com.hotel.entity.*;

public interface BookingRepository extends JpaRepository <Booking,Long> {
	// any overlap exists?
	  @Query("""
	    select count(b)>0 from Booking b
	    where b.room = :room
	      and b.checkIn < :checkOut
	      and b.checkOut > :checkIn
	  """)
	  boolean existsOverlap(Room room, LocalDate checkIn, LocalDate checkOut);
}