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

    @PreAuthorize("hasRole('RENTER')")
    @GetMapping("/history")
    public ResponseEntity<List<Rental>> rentalHistory(@RequestParam Long userId) {
        return ResponseEntity.ok(rentalService.getRentalHistory(userId));
    }

}
