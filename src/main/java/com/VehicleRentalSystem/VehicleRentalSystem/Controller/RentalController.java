package com.VehicleRentalSystem.VehicleRentalSystem.Controller;

import com.VehicleRentalSystem.VehicleRentalSystem.Model.Rental;
import com.VehicleRentalSystem.VehicleRentalSystem.Service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rentals")
@CrossOrigin(origins = "http://localhost:4200") // Change to frontend URL

public class RentalController {

    @Autowired
    private RentalService rentalService;


    @PostMapping("/extend")
    public ResponseEntity<Rental> extendRental(@RequestParam Long rentalId) {
        Rental updatedRental = rentalService.extendRental(rentalId);
        return ResponseEntity.ok(updatedRental);
    }


    @PutMapping("/return/{rentalId}")
    public ResponseEntity<Map<String, Object>> returnVehicle(
            @PathVariable Long rentalId,
            @RequestParam int kmsDriven,
            @RequestParam String damageLevel,
            @RequestParam String paymentMethod) {

        return rentalService.returnVehicle(rentalId, kmsDriven, damageLevel, paymentMethod);
    }





    @PostMapping("/rent/{vehicleId}")
    public ResponseEntity<String> rentVehicle(
            @PathVariable Long vehicleId,
            @RequestParam Long userId) {

        String response = rentalService.rentVehicle(userId, vehicleId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/all")
    public ResponseEntity<List<Rental>> getAllRentals() {
        return ResponseEntity.ok(rentalService.getAllOrdersAdmin());
    }


}
