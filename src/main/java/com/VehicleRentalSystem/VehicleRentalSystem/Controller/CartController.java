package com.VehicleRentalSystem.VehicleRentalSystem.Controller;

import com.VehicleRentalSystem.VehicleRentalSystem.Model.Cart;
import com.VehicleRentalSystem.VehicleRentalSystem.Model.Vehicle;
import com.VehicleRentalSystem.VehicleRentalSystem.Service.CartService;
import com.VehicleRentalSystem.VehicleRentalSystem.Service.RentalService;
import com.VehicleRentalSystem.VehicleRentalSystem.Service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cart")

@CrossOrigin(origins = "http://localhost:4200")
public class CartController {


    @Autowired
    private CartService cartService;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private RentalService rentalService;

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestParam Long userId, @RequestParam Long vehicleId) {
        cartService.addToCart(userId, vehicleId);
        return ResponseEntity.ok("✅ Vehicle added to cart successfully!");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Vehicle>> getUserCart(@PathVariable Long userId) {
        List<Cart> cartItems = cartService.getCartByUserId(userId);
        List<Long> vehicleIds = cartItems.stream().map(Cart::getVehicleId).collect(Collectors.toList());
        List<Vehicle> vehicles = vehicleService.getVehiclesByIds(vehicleIds);
        return ResponseEntity.ok(vehicles);
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