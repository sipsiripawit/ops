package com.yipintsoi.vehicleservice.service;

import com.yipintsoi.vehicleservice.domain.dto.BookingDTO;
import java.util.List;

public interface BookingService {
    List<BookingDTO> getAllBookings();
    BookingDTO getBookingById(Long id);
    BookingDTO createBooking(BookingDTO bookingDTO);
    BookingDTO updateBooking(Long id, BookingDTO bookingDTO);
    void deleteBooking(Long id);
}
