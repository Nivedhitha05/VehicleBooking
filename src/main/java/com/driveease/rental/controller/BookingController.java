package com.driveease.rental.controller;

import com.driveease.rental.dto.BookingDTO;
import com.driveease.rental.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    /** POST /api/bookings  — logged-in user creates a booking */
    @PostMapping
    public ResponseEntity<BookingDTO.Response> createBooking(
            @RequestBody BookingDTO.CreateRequest request,
            Authentication auth) {
        return ResponseEntity.ok(
                bookingService.createBooking(auth.getName(), request));
    }

    /** GET /api/bookings/my  — user sees their own bookings */
    @GetMapping("/my")
    public ResponseEntity<List<BookingDTO.Response>> myBookings(Authentication auth) {
        return ResponseEntity.ok(bookingService.getBookingsByUser(auth.getName()));
    }

    /** GET /api/bookings  — ADMIN: all bookings */
    @GetMapping
    public ResponseEntity<List<BookingDTO.Response>> allBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    /** PUT /api/bookings/{id}/cancel */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingDTO.Response> cancelBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }

    /** GET /api/bookings/dashboard  — Admin stats */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard() {
        return ResponseEntity.ok(bookingService.getDashboardStats());
    }
}
