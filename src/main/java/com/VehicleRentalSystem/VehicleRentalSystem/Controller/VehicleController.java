package com.VehicleRentalSystem.VehicleRentalSystem.Controller;

import com.VehicleRentalSystem.VehicleRentalSystem.Model.Vehicle;
//import com.VehicleRentalSystem.VehicleRentalSystem.Service.JWTService;
import com.VehicleRentalSystem.VehicleRentalSystem.Service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vehicles")
@CrossOrigin(origins = "http://localhost:4200")
public class VehicleController {

//    @Autowired
//    private JWTService jwtService;

    @Autowired
    private VehicleService vehicleService;

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVehicle(@PathVariable Long id) {
        boolean deleted = vehicleService.deleteVehicle(id);
        if (deleted) {
            return ResponseEntity.ok("Vehicle deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle not found");
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id, @RequestBody Vehicle updatedVehicle) {
        Vehicle vehicle = vehicleService.updateVehicle(id, updatedVehicle);
        return ResponseEntity.ok(vehicle);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.findAll());
    }

//    @PreAuthorize("hasRole('ADMIN')")
//    @PostMapping("/add")
//    public String addVehicle(@RequestBody Vehicle vehicle) {
//        vehicleService.addVehicle(vehicle);
//        return "Vehicle added successfully!";
//    }
@PostMapping("/add")
    public ResponseEntity<Vehicle> addVehicle(@RequestBody Vehicle vehicle) {
        Vehicle savedVehicle = vehicleService.addVehicle(vehicle);
        return ResponseEntity.ok(savedVehicle);
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // Save the file (dummy implementation, replace with actual storage logic)
            String fileUrl = "Imgs/" + file.getOriginalFilename();
//            String fileUrl = "http://localhost:8080/uploads/" + file.getOriginalFilename();
            Map<String, String> response = new HashMap<>();
            response.put("message", "File uploaded successfully");
            response.put("fileUrl", fileUrl);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

//    @PreAuthorize("hasRole('ADMIN')")
//    @DeleteMapping("/delete")
//    public ResponseEntity<String> deleteVehicle(@RequestParam Long id) {
//        if (vehicleService.existsById(id)) {
//            vehicleService.deleteById(id);
//            return ResponseEntity.ok("Vehicle deleted successfully!");
//        } else {
//            return ResponseEntity.badRequest().body("Vehicle not found!");
//        }
//    }

//    @PreAuthorize("hasRole('ADMIN')")
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

    @GetMapping("/search")
    public ResponseEntity<List<Vehicle>> searchVehicles(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String numberPlate,
            @RequestParam(required = false) Integer availableCount,
            @RequestParam(required = false, defaultValue = "name") String sortBy) {
        List<Vehicle> vehicles = vehicleService.searchVehicles(name, numberPlate, availableCount, sortBy);
        return ResponseEntity.ok(vehicles);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/markForService")
    public ResponseEntity<List<Vehicle>> markVehiclesForService() {
        List<Vehicle> vehicles = vehicleService.markVehiclesForService();
        return ResponseEntity.ok(vehicles);
    }


//    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/service/{vehicleId}")
    public ResponseEntity<String> serviceVehicle(@PathVariable Long vehicleId) {
        String response = vehicleService.serviceVehicle(vehicleId);
        return ResponseEntity.ok(response);
    }






}
