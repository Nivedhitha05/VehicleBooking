package com.driveease.rental.repository;

import com.driveease.rental.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByAvailableTrue();

    List<Vehicle> findByType(String type);

    List<Vehicle> findByAvailableTrueAndType(String type);

    @Query("SELECT v FROM Vehicle v WHERE " +
           "(:type IS NULL OR v.type = :type) AND " +
           "(:search IS NULL OR LOWER(v.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Vehicle> searchVehicles(@Param("type") String type,
                                  @Param("search") String search);
}
