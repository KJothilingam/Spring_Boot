package com.VehicleRentalSystem.VehicleRentalSystem.Service;


import com.VehicleRentalSystem.VehicleRentalSystem.Model.Cart;
import com.VehicleRentalSystem.VehicleRentalSystem.Model.Users;
import com.VehicleRentalSystem.VehicleRentalSystem.Model.Vehicle;
import com.VehicleRentalSystem.VehicleRentalSystem.Repository.CartRepository;
import com.VehicleRentalSystem.VehicleRentalSystem.Repository.RentalRepository;
import com.VehicleRentalSystem.VehicleRentalSystem.Repository.UserRepository;
import com.VehicleRentalSystem.VehicleRentalSystem.Repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Transactional
    public ResponseEntity<String> addToCart(Long userId, Long vehicleId) {
        Optional<Users> userOpt = userRepository.findById(userId);
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vehicleId);

        if (userOpt.isEmpty() || vehicleOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("❌ User or Vehicle not found!");
        }

        Users user = userOpt.get();
        Vehicle vehicle = vehicleOpt.get();

        if (cartRepository.findByUserAndVehicle(user, vehicle).isPresent()) {
            return ResponseEntity.badRequest().body("⚠️ Vehicle is already in the cart!");
        }

        Cart cart = new Cart(user, vehicle);
        cartRepository.save(cart);

        return ResponseEntity.ok("✅ Vehicle added to cart successfully!");
    }

    public List<Cart> getCartByUserId(Long userId) {
        return cartRepository.findByUser_UserId(userId);
    }

    public List<Cart> viewCart(Long userId) {
        Optional<Users> userOpt = userRepository.findById(userId);
        return userOpt.map(cartRepository::findByUser).orElse(null); // ✅ Change from findByRenter to findByUser
    }
    @Transactional
    public String removeFromCart(Long userId, Long vehicleId) {
        Optional<Users> userOpt = userRepository.findById(userId);
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vehicleId);

        if (userOpt.isEmpty() || vehicleOpt.isEmpty()) {
            return "❌ User or Vehicle not found!";
        }

        Users user = userOpt.get();
        Vehicle vehicle = vehicleOpt.get();
        Optional<Cart> cartItem = cartRepository.findByUserAndVehicle(user, vehicle);

        if (cartItem.isPresent()) {
            cartRepository.delete(cartItem.get());  // ✅ Correct delete operation
            return "✅ Vehicle removed from cart successfully!";
        } else {
            return "⚠️ Vehicle not found in cart!";
        }
    }


    public List<Vehicle> getVehiclesInCart(Long userId) {
        List<Cart> carts = cartRepository.findByUser_UserId(userId);
        return carts.stream()
                .map(cart -> vehicleRepository.findById(cart.getVehicleId()).orElse(null))
                .collect(Collectors.toList());
    }

}