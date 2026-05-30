package com.driveease.rental.service;

import com.driveease.rental.dto.BookingDTO;
import com.driveease.rental.model.Booking;
import com.driveease.rental.model.User;
import com.driveease.rental.model.Vehicle;
import com.driveease.rental.repository.BookingRepository;
import com.driveease.rental.repository.UserRepository;
import com.driveease.rental.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.18"); // 18% GST

    private final BookingRepository  bookingRepository;
    private final UserRepository     userRepository;
    private final VehicleRepository  vehicleRepository;

    @Transactional
    public BookingDTO.Response createBooking(String username, BookingDTO.CreateRequest req) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Vehicle vehicle = vehicleRepository.findById(req.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        if (!vehicle.getAvailable())
            throw new RuntimeException("Vehicle is not available for booking");

        // Calculate amounts
        BigDecimal base  = vehicle.getPricePerDay()
                                  .multiply(new BigDecimal(req.getNumDays()));
        BigDecimal tax   = base.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = base.add(tax);

        String ref = "BK" + System.currentTimeMillis() % 100000;

        Booking booking = Booking.builder()
                .bookingRef(ref)
                .user(user)
                .vehicle(vehicle)
                .numDays(req.getNumDays())
                .baseAmount(base)
                .taxAmount(tax)
                .totalAmount(total)
                .status(Booking.Status.ACTIVE)
                .bookingDate(LocalDate.now())
                .build();

        bookingRepository.save(booking);

        // Mark vehicle as unavailable
        vehicle.setAvailable(false);
        vehicleRepository.save(vehicle);

        return toResponse(booking);
    }

    public List<BookingDTO.Response> getBookingsByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return bookingRepository.findByUserId(user.getId())
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<BookingDTO.Response> getAllBookings() {
        return bookingRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public BookingDTO.Response cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(Booking.Status.CANCELLED);
        booking.getVehicle().setAvailable(true);
        vehicleRepository.save(booking.getVehicle());
        return toResponse(bookingRepository.save(booking));
    }

    // Admin dashboard stats
    public java.util.Map<String, Object> getDashboardStats() {
        return java.util.Map.of(
                "totalVehicles",  vehicleRepository.count(),
                "availableVehicles", vehicleRepository.findByAvailableTrue().size(),
                "totalBookings",  bookingRepository.count(),
                "totalRevenue",   bookingRepository.getTotalRevenue()
        );
    }

    private BookingDTO.Response toResponse(Booking b) {
        BookingDTO.Response r = new BookingDTO.Response();
        r.setId(b.getId());
        r.setBookingRef(b.getBookingRef());
        r.setUsername(b.getUser().getUsername());
        r.setVehicleName(b.getVehicle().getName());
        r.setVehicleEmoji(b.getVehicle().getEmoji());
        r.setNumDays(b.getNumDays());
        r.setBaseAmount(b.getBaseAmount());
        r.setTaxAmount(b.getTaxAmount());
        r.setTotalAmount(b.getTotalAmount());
        r.setStatus(b.getStatus().name());
        r.setBookingDate(b.getBookingDate());
        return r;
    }
}
