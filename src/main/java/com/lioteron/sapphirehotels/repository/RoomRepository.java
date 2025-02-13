package com.lioteron.sapphirehotels.repository;

import com.lioteron.sapphirehotels.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT DISTINCT r FROM Room r " +
           "WHERE r.hotel.id = :hotelId " +
           "AND r.maxGuests >= :guests " +
           "AND r.available = true " +
           "AND NOT EXISTS (" +
           "    SELECT b FROM Booking b " +
           "    WHERE b.room = r " +
           "    AND b.status != 'cancelled' " +
           "    AND (" +
           "        (b.checkIn <= :checkOut AND b.checkOut >= :checkIn)" +
           "    )" +
           ")")
    List<Room> findAvailableRooms(
        @Param("hotelId") Long hotelId,
        @Param("checkIn") LocalDate checkIn,
        @Param("checkOut") LocalDate checkOut,
        @Param("guests") Integer guests
    );
}