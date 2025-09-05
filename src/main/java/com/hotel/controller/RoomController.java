package com.hotel.controller;

import com.hotel.dto.RoomRequest;
import com.hotel.entity.Room;
import com.hotel.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController @RequestMapping("/api/rooms")
public class RoomController {
  private final RoomService rooms;

  public RoomController(RoomService rooms) { this.rooms = rooms; }

  // (1) Create/Edit/Delete own listings
  @PostMapping
  public Room create(@Valid @RequestBody RoomRequest req) { return rooms.create(req); }

  @PutMapping("/{id}")
  public Room update(@PathVariable Long id, @Valid @RequestBody RoomRequest req) { return rooms.update(id, req); }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) { rooms.delete(id); }

  @GetMapping("/mine")
  public List<Room> myRooms() { return rooms.myRooms(); }

  // (2) Search available rooms by date range
  @GetMapping("/search")
  public List<Room> search(
      @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate checkIn,
      @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate checkOut) {
    return rooms.searchAvailable(checkIn, checkOut);
  }
}