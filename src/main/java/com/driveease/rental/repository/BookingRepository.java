package com.driveease.rental.repository;

import com.driveease.rental.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);

    Optional<Booking> findByBookingRef(String bookingRef);

    List<Booking> findByVehicleId(Long vehicleId);

    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM Booking b WHERE b.status = 'ACTIVE'")
    BigDecimal getTotalRevenue();

    long countByStatus(Booking.Status status);
}
