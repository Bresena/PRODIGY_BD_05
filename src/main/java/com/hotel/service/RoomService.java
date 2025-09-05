package com.hotel.service;

import com.hotel.dto.RoomRequest;
import com.hotel.entity.*;
import com.hotel.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class RoomService {
  private final RoomRepository rooms;
  private final UserRepository users;

  public RoomService(RoomRepository rooms, UserRepository users) {
    this.rooms = rooms; this.users = users;
  }

  private User currentUser() {
    Authentication a = SecurityContextHolder.getContext().getAuthentication();
    var email = a.getName();
    return users.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));
  }

  public Room create(RoomRequest req) {
    var owner = currentUser();
    var r = Room.builder()
        .title(req.title())
        .description(req.description())
        .pricePerNight(req.pricePerNight())
        .owner(owner).build();
    return rooms.save(r);
  }

  public Room update(Long id, RoomRequest req) {
    var owner = currentUser();
    var room = rooms.findById(id).orElseThrow(() -> new EntityNotFoundException("Room not found"));
    if (!room.getOwner().getId().equals(owner.getId())) throw new SecurityException("Not your room");
    room.setTitle(req.title());
    room.setDescription(req.description());
    room.setPricePerNight(req.pricePerNight());
    return rooms.save(room);
  }

  public void delete(Long id) {
    var owner = currentUser();
    var room = rooms.findById(id).orElseThrow(() -> new EntityNotFoundException("Room not found"));
    if (!room.getOwner().getId().equals(owner.getId())) throw new SecurityException("Not your room");
    rooms.delete(room);
  }

  public List<Room> myRooms() {
    var owner = currentUser();
    return rooms.findByOwnerId(owner.getId());
  }

  public List<Room> searchAvailable(LocalDate checkIn, LocalDate checkOut) {
    return rooms.findAvailable(checkIn, checkOut);
  }
}