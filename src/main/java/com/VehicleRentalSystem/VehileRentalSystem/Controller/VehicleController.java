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
@RequestMapping("/vehicle")
public class VehicleController {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/list")
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.findAll());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add")
    public String addVehicle(@RequestBody Vehicle vehicle) {
        vehicleService.addVehicle(vehicle);
        return "Vehicle added successfully!";
    }

//    @PreAuthorize("hasAuthority('ADMIN')")
//    @PutMapping("/update")
//    public String updateVehicle(@RequestBody Vehicle vehicle) {
//        vehicleService.UpdateVehicle(vehicle);
//        return "Vehicle updated successfully!";
//    }
//
//    @PreAuthorize("hasAuthority('ADMIN')")
//    @DeleteMapping("/delete")
//    public String deleteVehicle(@RequestParam Long id) {
//        return "Vehicle deleted successfully!";
//    }



}
