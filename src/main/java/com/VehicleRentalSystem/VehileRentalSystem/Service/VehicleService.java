package com.VehicleRentalSystem.VehileRentalSystem.Service;

import com.VehicleRentalSystem.VehileRentalSystem.Model.Users;
import com.VehicleRentalSystem.VehileRentalSystem.Model.Vehicle;
import com.VehicleRentalSystem.VehileRentalSystem.Repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

//    public void UpdateVehicle(Vehicle vehicle) {
//        return repository.saveAll(vehicle);
//    }
}
