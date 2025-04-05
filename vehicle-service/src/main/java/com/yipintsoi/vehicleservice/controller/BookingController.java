package com.yipintsoi.vehicleservice.controller;

import com.yipintsoi.vehicleservice.domain.dto.BookingDTO;
import com.yipintsoi.vehicleservice.response.ApiResponse;
import com.yipintsoi.vehicleservice.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookingDTO>>> getAllBookings() {
        List<BookingDTO> bookings = bookingService.getAllBookings();
        ApiResponse<List<BookingDTO>> response = ApiResponse.<List<BookingDTO>>builder()
                .status("success")
                .data(bookings)
                .message("Fetched all bookings successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingDTO>> getBookingById(@PathVariable Long id) {
        BookingDTO booking = bookingService.getBookingById(id);
        ApiResponse<BookingDTO> response = ApiResponse.<BookingDTO>builder()
                .status("success")
                .data(booking)
                .message("Fetched booking successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookingDTO>> createBooking(@RequestBody BookingDTO bookingDTO) {
        BookingDTO created = bookingService.createBooking(bookingDTO);
        ApiResponse<BookingDTO> response = ApiResponse.<BookingDTO>builder()
                .status("success")
                .data(created)
                .message("Booking created successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingDTO>> updateBooking(@PathVariable Long id, @RequestBody BookingDTO bookingDTO) {
        BookingDTO updated = bookingService.updateBooking(id, bookingDTO);
        ApiResponse<BookingDTO> response = ApiResponse.<BookingDTO>builder()
                .status("success")
                .data(updated)
                .message("Booking updated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status("success")
                .message("Booking deleted successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}
