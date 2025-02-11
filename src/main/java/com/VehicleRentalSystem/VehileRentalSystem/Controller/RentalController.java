package com.VehicleRentalSystem.VehileRentalSystem.Controller;

import com.VehicleRentalSystem.VehileRentalSystem.Model.Rental;
import com.VehicleRentalSystem.VehileRentalSystem.Service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @PostMapping("/checkout")
    public ResponseEntity<String> checkoutCart(@RequestParam Long userId) {
        String response = rentalService.checkoutCart(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/extend")
    public ResponseEntity<String> extendRental(@RequestParam Long rentalId) {
        String response = rentalService.extendRental(rentalId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<List<Rental>> rentalHistory(@RequestParam Long userId) {
        return ResponseEntity.ok(rentalService.getRentalHistory(userId));
    }

//    @PostMapping("/return")
//    public ResponseEntity<String> returnVehicle(
//            @RequestParam Long rentalId,
//            @RequestParam int kmsDriven,
//            @RequestParam String damageLevel,
//            @RequestParam boolean paidByCash) {
//
//        String response = rentalService.returnVehicle(rentalId, kmsDriven, damageLevel, paidByCash);
//        return ResponseEntity.ok(response);
//    }


}
