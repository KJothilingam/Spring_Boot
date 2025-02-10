package com.VehicleRentalSystem.VehileRentalSystem.Controller;

import com.VehicleRentalSystem.VehileRentalSystem.Model.Users;
import com.VehicleRentalSystem.VehileRentalSystem.Model.Vehicle;
import com.VehicleRentalSystem.VehileRentalSystem.Service.JWTService;
import com.VehicleRentalSystem.VehileRentalSystem.Service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/list")
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public String addVehicle(@RequestBody Vehicle vehicle) {
//        vehicleService.addVehicle(vehicle);
        return "Vehicle added successfully!";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteVehicle(@RequestParam Long id) {
        if (vehicleService.existsById(id)) {
            vehicleService.deleteById(id);
            return ResponseEntity.ok("Vehicle deleted successfully!");
        } else {
            return ResponseEntity.badRequest().body("Vehicle not found!");
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<String> updateVehicle(@RequestBody Vehicle vehicle) {
        if (vehicle.getId() == null) {
            return ResponseEntity.badRequest().body("Vehicle ID is required for update!");
        }

        if (!vehicleService.existsById(vehicle.getId())) {
            return ResponseEntity.badRequest().body("Vehicle not found!");
        }

        vehicleService.updateVehicle(vehicle);
        return ResponseEntity.ok("Vehicle updated successfully!");
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<List<Vehicle>> searchVehicles(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String numberPlate,
            @RequestParam(required = false) Integer availableCount,
            @RequestParam(required = false, defaultValue = "name") String sortBy) {

        List<Vehicle> vehicles = vehicleService.searchVehicles(name, numberPlate, availableCount, sortBy);
        return ResponseEntity.ok(vehicles);
    }






}
