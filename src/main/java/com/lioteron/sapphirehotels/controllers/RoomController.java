package com.lioteron.sapphirehotels.controllers;

import com.lioteron.sapphirehotels.model.Booking;
import com.lioteron.sapphirehotels.model.Room;
import com.lioteron.sapphirehotels.model.User;
import com.lioteron.sapphirehotels.service.BookingService;
import com.lioteron.sapphirehotels.service.RoomService;
import com.lioteron.sapphirehotels.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @GetMapping("/available")
    public ResponseEntity<List<Room>> getAvailableRooms(
            @RequestParam Long hotelId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam Integer guests) {
        List<Room> availableRooms = roomService.findAvailableRooms(hotelId, checkIn, checkOut, guests);
        return ResponseEntity.ok(availableRooms);
    }

    @PostMapping("/{roomId}/book")
    public ResponseEntity<Booking> bookRoom(
            @PathVariable Long roomId,
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {
        User user = userService.findById(userId);
        Room room = roomService.findById(roomId);

        if (checkIn.isAfter(checkOut)) {
            throw new IllegalArgumentException("Check-in date must be before check-out date");
        }

        if (checkIn.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Check-in date cannot be in the past");
        }

        // Verify room availability
        List<Room> availableRooms = roomService.findAvailableRooms(
            room.getHotel().getId(),
            checkIn,
            checkOut,
            1  // Minimum guests
        );

        if (availableRooms.stream().noneMatch(r -> r.getId().equals(roomId))) {
            throw new IllegalStateException("Room is not available for the selected dates");
        }

        Booking booking = bookingService.createBooking(user, room, checkIn, checkOut);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/{roomId}/bookings")
    public ResponseEntity<List<Booking>> getRoomBookings(
            @PathVariable Long roomId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        if (from == null) {
            from = LocalDate.now();
        }
        if (to == null) {
            to = from.plusMonths(1);
        }

        List<Booking> bookings = bookingService.findBookingsByRoomAndDateRange(roomId, from, to);
        return ResponseEntity.ok(bookings);
    }

    @DeleteMapping("/bookings/{bookingId}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.noContent().build();
    }
}