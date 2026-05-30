package com.driveease.rental.service;

import com.driveease.rental.model.Vehicle;
import com.driveease.rental.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public List<Vehicle> searchVehicles(String type, String search) {
        String typeParam   = (type   != null && !type.isBlank()   && !"All".equals(type))   ? type   : null;
        String searchParam = (search != null && !search.isBlank()) ? search : null;
        return vehicleRepository.searchVehicles(typeParam, searchParam);
    }

    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
    }

    public Vehicle addVehicle(Vehicle vehicle) {
        vehicle.setAvailable(true);
        return vehicleRepository.save(vehicle);
    }

    public Vehicle updateVehicle(Long id, Vehicle updated) {
        Vehicle existing = getVehicleById(id);
        existing.setName(updated.getName());
        existing.setType(updated.getType());
        existing.setEmoji(updated.getEmoji());
        existing.setPricePerDay(updated.getPricePerDay());
        existing.setSeats(updated.getSeats());
        existing.setFuelType(updated.getFuelType());
        if (updated.getAvailable() != null) {
            existing.setAvailable(updated.getAvailable());
        }
        return vehicleRepository.save(existing);
    }

    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }

    public void setAvailability(Long id, boolean available) {
        Vehicle v = getVehicleById(id);
        v.setAvailable(available);
        vehicleRepository.save(v);
    }
}
