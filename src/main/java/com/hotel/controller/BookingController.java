package com.hotel.controller;

import com.hotel.dto.BookingRequest;
import com.hotel.entity.Booking;
import com.hotel.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/bookings")
public class BookingController {
  private final BookingService bookings;
  public BookingController(BookingService bookings) { this.bookings = bookings; }

  // (3) Book a room (reserve if available)
  @PostMapping
  public Booking book(@Valid @RequestBody BookingRequest req) { return bookings.book(req); }

  @DeleteMapping("/{id}")
  public void cancel(@PathVariable Long id) { bookings.cancel(id); }
}