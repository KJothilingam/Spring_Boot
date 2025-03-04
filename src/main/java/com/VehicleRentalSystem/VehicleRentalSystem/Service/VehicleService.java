package com.VehicleRentalSystem.VehicleRentalSystem.Service;

import com.VehicleRentalSystem.VehicleRentalSystem.Model.Vehicle;
import com.VehicleRentalSystem.VehicleRentalSystem.Repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class VehicleService {

    @Autowired
    private VehicleRepository repository;

    public List<Vehicle> getVehiclesByIds(List<Long> ids) {
        return repository.findAllById(ids);
    }

    @Transactional
    public List<Vehicle> markVehiclesForService() {

        List<Vehicle> bikesToService = repository.findBikesForService();

        List<Vehicle> carsToService = repository.findCarsForService();

        List<Vehicle> vehiclesToService = new ArrayList<>();
        vehiclesToService.addAll(bikesToService);
        vehiclesToService.addAll(carsToService);

        if (vehiclesToService.isEmpty()) {
            return List.of();
        }
        for (Vehicle vehicle : vehiclesToService) {
            vehicle.setNeedsService(true);
            repository.save(vehicle);
        }

        return vehiclesToService;
    }

    @Transactional
    public String serviceVehicle(Long vehicleId) {
        Vehicle vehicle = repository.findById(vehicleId).orElse(null);
        if (vehicle == null) {
            return "Vehicle not found!";
        }
        if (!vehicle.isNeedsService()) {
            return "Vehicle does not require servicing!";
        }
        if ((vehicle.getType().equalsIgnoreCase("BIKE") && vehicle.getLastServiceAt() < 1500) ||
                (vehicle.getType().equalsIgnoreCase("CAR") && vehicle.getLastServiceAt() < 3000)) {
            return "Cannot service this vehicle yet! It hasn't reached the required kilometers.";
        }
        vehicle.setLastServiceAt(0);
        vehicle.setNeedsService(false);
        repository.save(vehicle);

        return "Vehicle serviced successfully!";
    }

    public List<Vehicle> findAll() {
        return repository.findAll();
    }

//    public Vehicle addVehicle(Vehicle vehicle) {
//        return repository.save(vehicle);
//    }
public Vehicle updateVehicle(Long id, Vehicle updatedVehicle) {
    Optional<Vehicle> existingVehicleOptional = repository.findById(id);

    if (existingVehicleOptional.isPresent()) {
        Vehicle existingVehicle = existingVehicleOptional.get();

        existingVehicle.setAvailable(updatedVehicle.getAvailable());
        existingVehicle.setName(updatedVehicle.getName());
        existingVehicle.setNeedsService(updatedVehicle.isNeedsService());
        existingVehicle.setNumberPlate(updatedVehicle.getNumberPlate());
        existingVehicle.setRentalPrice(updatedVehicle.getRentalPrice());
        existingVehicle.setTotalKmsDriven(updatedVehicle.getTotalKmsDriven());
        existingVehicle.setType(updatedVehicle.getType());
        existingVehicle.setLastServiceAt(updatedVehicle.getLastServiceAt());
        existingVehicle.setImageUrl(updatedVehicle.getImageUrl());

        return repository.save(existingVehicle); // Save and return updated vehicle
    } else {
        throw new RuntimeException("Vehicle not found with ID: " + id);
    }
}

public boolean deleteVehicle(Long id) {
    if (repository.existsById(id)) {
        repository.deleteById(id);
        return true;
    }
    return false;
}

    public Vehicle addVehicle(Vehicle vehicle) {
        System.out.println("Saving vehicle: " + vehicle);
        return repository.save(vehicle);
    }


    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void updateVehicle(Vehicle vehicle) {
        repository.save(vehicle);
    }

    public List<Vehicle> searchVehicles(String name, String numberPlate, Integer availableCount, String sortBy) {
        Sort sort = Sort.unsorted();
        if ("name".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(Sort.Direction.ASC, "name");
        } else if ("availableCount".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(Sort.Direction.DESC, "availableCount");
        }

        if (name != null) {
            return repository.findByNameContainingIgnoreCase(name, sort);
        } else if (numberPlate != null) {
            return repository.findByNumberPlateContainingIgnoreCase(numberPlate, sort);
        }
//        else if (availableCount != null) {
//            return repository.findByAvailableCountGreaterThanEqual(availableCount, sort);
//        }
        else {
            return repository.findAll(sort);
        }
    }



}
