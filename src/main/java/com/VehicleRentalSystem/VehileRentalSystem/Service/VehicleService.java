package com.VehicleRentalSystem.VehileRentalSystem.Service;

import com.VehicleRentalSystem.VehileRentalSystem.Model.Users;
import com.VehicleRentalSystem.VehileRentalSystem.Model.Vehicle;
import com.VehicleRentalSystem.VehileRentalSystem.Repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository repository;

    @Transactional
    public List<Vehicle> markVehiclesForService() {
        // ✅ Find Bikes (`lastServiceAt > 1500`)
        List<Vehicle> bikesToService = repository.findBikesForService();
        // ✅ Find Cars (`lastServiceAt > 3000`)
        List<Vehicle> carsToService = repository.findCarsForService();

        // ✅ Combine both lists
        List<Vehicle> vehiclesToService = new ArrayList<>();
        vehiclesToService.addAll(bikesToService);
        vehiclesToService.addAll(carsToService);

        if (vehiclesToService.isEmpty()) {
            return List.of(); // Return empty list if no servicing needed
        }

        // ✅ Mark each vehicle as needing service
        for (Vehicle vehicle : vehiclesToService) {
            vehicle.setNeedsService(true);
            repository.save(vehicle);
        }

        return vehiclesToService; // Return the updated list of vehicles needing service
    }




    @Transactional
    public String serviceVehicle(Long vehicleId) {
        Vehicle vehicle = repository.findById(vehicleId).orElse(null);
        if (vehicle == null) {
            return "Vehicle not found!";
        }

        // 🚗 **Check if the vehicle is actually due for servicing**
        if (!vehicle.isNeedsService()) {
            return "Vehicle does not require servicing!";
        }

        // 🚗 **Ensure the vehicle has exceeded the servicing threshold**
        if ((vehicle.getType().equalsIgnoreCase("BIKE") && vehicle.getLastServiceAt() < 1500) ||
                (vehicle.getType().equalsIgnoreCase("CAR") && vehicle.getLastServiceAt() < 3000)) {
            return "Cannot service this vehicle yet! It hasn't reached the required kilometers.";
        }
        // ✅ **Perform Servicing: Reset `lastServiceAt` and `needsService`**
        vehicle.setLastServiceAt(0);
        vehicle.setNeedsService(false);
        repository.save(vehicle);

        return "Vehicle serviced successfully!";
    }



    public List<Vehicle> findAll() {
        return repository.findAll();
    }

    public Vehicle addVehicle(Vehicle vehicle) {
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
        // Set sorting options
        if ("name".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(Sort.Direction.ASC, "name");
        } else if ("availableCount".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(Sort.Direction.DESC, "availableCount");
        }

        // If search parameters are provided, filter accordingly
        if (name != null) {
            return repository.findByNameContainingIgnoreCase(name, sort);
        } else if (numberPlate != null) {
            return repository.findByNumberPlateContainingIgnoreCase(numberPlate, sort);
        } else if (availableCount != null) {
            return repository.findByAvailableCountGreaterThanEqual(availableCount, sort);
        } else {
            return repository.findAll(sort);
        }
    }


}
