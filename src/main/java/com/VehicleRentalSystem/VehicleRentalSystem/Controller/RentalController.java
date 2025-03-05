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

@RestController
@RequestMapping("/rentals")
@CrossOrigin(origins = "http://localhost:4200") // Change to frontend URL

public class RentalController {

    @Autowired
    private RentalService rentalService;

//    @PostMapping("/checkout")
//    public ResponseEntity<String> checkoutCart(@RequestParam Long userId) {
//        String response = rentalService.checkoutCart(userId);
//        return ResponseEntity.ok(response);
//    }
//
//import org.springframework.http.MediaType;

    @PostMapping("/extend")
    public ResponseEntity<String> extendRental(@RequestParam Long rentalId) {
        String response = rentalService.extendRental(rentalId);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN) // ✅ Ensures it's treated as plain text
                .body(response);
    }

    @PutMapping("/return/{rentalId}")
    public ResponseEntity<String> returnVehicle(
            @PathVariable Long rentalId,
            @RequestParam int kmsDriven,
            @RequestParam String damageLevel,
            @RequestParam boolean paidByCash) {

        String response = rentalService.returnVehicle(rentalId, kmsDriven, damageLevel, paidByCash);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)  // ✅ Ensure response is plain text
                .body(response);
    }



//
//    @GetMapping("/history")
//    public ResponseEntity<List<Rental>> rentalHistory(@RequestParam Long userId) {
//        return ResponseEntity.ok(rentalService.getRentalHistory(userId));
//    }
//
//    @PutMapping("/return/{rentalId}")
//    public ResponseEntity<String> returnVehicle(
//            @PathVariable Long rentalId,
//            @RequestParam int kmsDriven,
//            @RequestParam String damageLevel,
//            @RequestParam boolean paidByCash) {
//
//        String response = rentalService.returnVehicle(rentalId, kmsDriven, damageLevel, paidByCash);
//        return ResponseEntity.ok(response);
//    }
//
//
////    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping("/all-rented")
//    public ResponseEntity<List<Rental>> getAllRentedVehicles() {
//        List<Rental> rentals = rentalService.getAllRentedVehicles();
//        return ResponseEntity.ok(rentals);
//    }
//
//
////    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping("/unreturned")
//    public ResponseEntity<List<Rental>> getUnreturnedVehicles() {
//        List<Rental> unreturnedRentals = rentalService.getUnreturnedVehicles();
//        return ResponseEntity.ok(unreturnedRentals);
//    }


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


//@GetMapping("/{userId}")
//public ResponseEntity<List<Rental>> getUserRentals(@PathVariable Long userId) {
//    return ResponseEntity.ok(rentalService.getRentalsByUserId(userId));
//}






}
