package com.yipintsoi.vehicleservice.service.impl;

import com.yipintsoi.vehicleservice.domain.dto.BookingDTO;
import com.yipintsoi.vehicleservice.domain.entity.Booking;
import com.yipintsoi.vehicleservice.domain.entity.Vehicle;
import com.yipintsoi.vehicleservice.domain.mapper.BookingMapper;
import com.yipintsoi.vehicleservice.exception.CustomException;
import com.yipintsoi.vehicleservice.repository.BookingRepository;
import com.yipintsoi.vehicleservice.repository.VehicleRepository;
import com.yipintsoi.vehicleservice.service.BookingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final VehicleRepository vehicleRepository;
    private final BookingMapper bookingMapper;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              VehicleRepository vehicleRepository,
                              BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.vehicleRepository = vehicleRepository;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public List<BookingDTO> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream()
                .map(bookingMapper::bookingToBookingDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookingDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new CustomException("Booking not found with id: " + id));
        return bookingMapper.bookingToBookingDTO(booking);
    }

    @Override
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        // ตรวจสอบว่า vehicleId มีอยู่จริง
        Vehicle vehicle = vehicleRepository.findById(bookingDTO.getVehicleId())
                .orElseThrow(() -> new CustomException("Vehicle not found with id: " + bookingDTO.getVehicleId()));

        Booking booking = bookingMapper.bookingDTOToBooking(bookingDTO);
        booking.setVehicle(vehicle);
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());
        Booking saved = bookingRepository.save(booking);
        return bookingMapper.bookingToBookingDTO(saved);
    }

    @Override
    public BookingDTO updateBooking(Long id, BookingDTO bookingDTO) {
        Booking existing = bookingRepository.findById(id)
                .orElseThrow(() -> new CustomException("Booking not found with id: " + id));

        // ถ้ามีการเปลี่ยน vehicleId
        if (!existing.getVehicle().getId().equals(bookingDTO.getVehicleId())) {
            Vehicle vehicle = vehicleRepository.findById(bookingDTO.getVehicleId())
                    .orElseThrow(() -> new CustomException("Vehicle not found with id: " + bookingDTO.getVehicleId()));
            existing.setVehicle(vehicle);
        }
        existing.setStartTime(bookingDTO.getStartTime());
        existing.setEndTime(bookingDTO.getEndTime());
        existing.setUpdatedAt(LocalDateTime.now());
        Booking updated = bookingRepository.save(existing);
        return bookingMapper.bookingToBookingDTO(updated);
    }

    @Override
    public void deleteBooking(Long id) {
        Booking existing = bookingRepository.findById(id)
                .orElseThrow(() -> new CustomException("Booking not found with id: " + id));
        bookingRepository.delete(existing);
    }
}
