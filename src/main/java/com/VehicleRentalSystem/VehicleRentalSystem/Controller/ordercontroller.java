package com.VehicleRentalSystem.VehicleRentalSystem.Controller;

import com.VehicleRentalSystem.VehicleRentalSystem.Model.Rental;
import com.VehicleRentalSystem.VehicleRentalSystem.Service.RentalService;
import com.VehicleRentalSystem.VehicleRentalSystem.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class  ordercontroller{
    @Autowired
    private RentalService rentalService;
    @Autowired
    private UserService userService;

    @GetMapping("/user/security-deposit/{userId}")
    public ResponseEntity<Double> getUserSecurityDeposit(@PathVariable Long userId) {
        double deposit = userService.getUserSecurityDeposit(userId);
        return ResponseEntity.ok(deposit);
    }


//    @GetMapping
//    public List<Rental> getAllOrders() {
//        return rentalService.getAllOrders();
//    }
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<Rental>> getUserOrders(@PathVariable Long userId) {
//        return ResponseEntity.ok(rentalService.getUserRentals(userId));
//    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Rental>> getUserActiveRentals(@PathVariable Long userId) {
        List<Rental> rentals = rentalService.getUserActiveRentals(userId);
        return ResponseEntity.ok(rentals);
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<Rental>> getUserHistory(@PathVariable Long userId) {
        List<Rental> rentals = rentalService.getUserHistory(userId);
        return ResponseEntity.ok(rentals);
    }
}
