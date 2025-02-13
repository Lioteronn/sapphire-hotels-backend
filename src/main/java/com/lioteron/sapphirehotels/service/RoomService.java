package com.lioteron.sapphirehotels.service;

import com.lioteron.sapphirehotels.model.Room;
import com.lioteron.sapphirehotels.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    public List<Room> findAvailableRooms(Long hotelId, LocalDate checkIn, LocalDate checkOut, Integer guests) {
        return roomRepository.findAvailableRooms(hotelId, checkIn, checkOut, guests);
    }

    public Room findById(Long id) {
        return roomRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
    }
}