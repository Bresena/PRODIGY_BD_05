package com.hotel.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.*;

import com.hotel.entity.Room;

public interface RoomRepository extends JpaRepository <Room,Long> {
	// rooms NOT having any booking overlapping (checkIn, checkOut)
	  @Query("""
	    select r from Room r
	    where not exists (
	      select b from Booking b
	      where b.room = r
	        and b.checkIn < :checkOut
	        and b.checkOut > :checkIn
	    )
	  """)
	  List<Room> findAvailable(LocalDate checkIn, LocalDate checkOut);

	  List<Room> findByOwnerId(Long ownerId);
}