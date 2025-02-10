package com.VehicleRentalSystem.VehileRentalSystem.Service;

import com.VehicleRentalSystem.VehileRentalSystem.Model.Users;
import com.VehicleRentalSystem.VehileRentalSystem.Model.Vehicle;
import com.VehicleRentalSystem.VehileRentalSystem.Repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository repository;

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

//    public void UpdateVehicle(Vehicle vehicle) {
//        return repository.saveAll(vehicle);
//    }
}
