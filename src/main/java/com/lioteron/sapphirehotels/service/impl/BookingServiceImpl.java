package com.lioteron.sapphirehotels.service.impl;

import com.lioteron.sapphirehotels.model.Booking;
import com.lioteron.sapphirehotels.model.Room;
import com.lioteron.sapphirehotels.model.User;
import com.lioteron.sapphirehotels.repository.BookingRepository;
import com.lioteron.sapphirehotels.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public Booking createBooking(User user, Room room, LocalDate checkIn, LocalDate checkOut) {
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setCheckInDate(checkIn);
        booking.setCheckOutDate(checkOut);
        booking.setStatus("CONFIRMED");

        // Calculate total price based on number of nights and room price
        long numberOfNights = ChronoUnit.DAYS.between(checkIn, checkOut);
        double totalPrice = numberOfNights * room.getPricePerNight().doubleValue();
        booking.setTotalPrice(BigDecimal.valueOf(totalPrice));

        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> findBookingsByRoomAndDateRange(Long roomId, LocalDate from, LocalDate to) {
        return bookingRepository.findByRoomIdAndCheckInDateBetween(roomId, from, to);
    }

    @Override
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findUserBookings(userId);
    }
}