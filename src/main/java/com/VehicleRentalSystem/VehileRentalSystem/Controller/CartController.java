package com.VehicleRentalSystem.VehileRentalSystem.Controller;

import com.VehicleRentalSystem.VehileRentalSystem.Model.Cart;
import com.VehicleRentalSystem.VehileRentalSystem.Service.CartService;
import com.VehicleRentalSystem.VehileRentalSystem.Service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private RentalService rentalService;

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestParam Long userId, @RequestParam Long vehicleId) {
        String response = cartService.addToCart(userId, vehicleId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/view")
    public ResponseEntity<List<Cart>> viewCart(@RequestParam Long userId) {
        return ResponseEntity.ok(cartService.viewCart(userId));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestParam Long userId, @RequestParam Long vehicleId) {
        String response = cartService.removeFromCart(userId, vehicleId);
        return ResponseEntity.ok(response);
    }


}
