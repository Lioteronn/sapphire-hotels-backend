package com.lioteron.sapphirehotels.repository;

import com.lioteron.sapphirehotels.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.room.id = :roomId " +
           "AND b.checkIn >= :startDate " +
           "AND b.checkOut <= :endDate " +
           "AND b.status != 'CANCELLED'")
    List<Booking> findByRoomIdAndCheckInDateBetween(
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId " +
           "ORDER BY b.checkIn DESC")
    List<Booking> findUserBookings(@Param("userId") Long userId);
}