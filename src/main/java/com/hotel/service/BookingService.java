package com.hotel.service;

import com.hotel.dto.BookingRequest;
import com.hotel.entity.Booking;
import com.hotel.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
  private final BookingRepository bookings;
  private final RoomRepository rooms;
  private final UserRepository users;

  public BookingService(BookingRepository bookings, RoomRepository rooms, UserRepository users) {
    this.bookings = bookings; this.rooms = rooms; this.users = users;
  }

  public Booking book(BookingRequest req) {
    if (!req.checkIn().isBefore(req.checkOut()))
      throw new ValidationException("checkIn must be before checkOut");

    var room = rooms.findById(req.roomId())
        .orElseThrow(() -> new EntityNotFoundException("Room not found"));

    boolean overlap = bookings.existsOverlap(room, req.checkIn(), req.checkOut());
    if (overlap) throw new ValidationException("Room not available for selected dates");

    var email = SecurityContextHolder.getContext().getAuthentication().getName();
    var guest = users.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));

    var booking = Booking.builder()
        .room(room).guest(guest)
        .checkIn(req.checkIn()).checkOut(req.checkOut())
        .build();
    return bookings.save(booking);
  }

  public void cancel(Long bookingId) {
    var b = bookings.findById(bookingId).orElseThrow(() -> new EntityNotFoundException("Booking not found"));
    // Allow guest or room owner to cancel
    var email = SecurityContextHolder.getContext().getAuthentication().getName();
    var actorEmail = email.toLowerCase();
    var guestEmail = b.getGuest().getEmail().toLowerCase();
    var ownerEmail = b.getRoom().getOwner().getEmail().toLowerCase();
    if (!actorEmail.equals(guestEmail) && !actorEmail.equals(ownerEmail))
      throw new SecurityException("Not allowed to cancel this booking");
    bookings.delete(b);
  }
}