package com.lioteron.sapphirehotels.service;

import com.lioteron.sapphirehotels.model.Booking;
import com.lioteron.sapphirehotels.model.Room;
import com.lioteron.sapphirehotels.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface BookingService {
    Booking createBooking(User user, Room room, LocalDate checkIn, LocalDate checkOut);
    List<Booking> findBookingsByRoomAndDateRange(Long roomId, LocalDate from, LocalDate to);
    void cancelBooking(Long bookingId);
    List<Booking> getUserBookings(Long userId);
}