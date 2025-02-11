package com.VehicleRentalSystem.VehileRentalSystem.Service;


import com.VehicleRentalSystem.VehileRentalSystem.Model.Cart;
import com.VehicleRentalSystem.VehileRentalSystem.Model.Users;
import com.VehicleRentalSystem.VehileRentalSystem.Model.Vehicle;
import com.VehicleRentalSystem.VehileRentalSystem.Repository.CartRepository;
import com.VehicleRentalSystem.VehileRentalSystem.Repository.RentalRepository;
import com.VehicleRentalSystem.VehileRentalSystem.Repository.UserRepository;
import com.VehicleRentalSystem.VehileRentalSystem.Repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

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

    public String addToCart(Long userId, Long vehicleId) {
        Optional<Users> userOpt = userRepository.findById(userId);
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vehicleId);
        if (userOpt.isEmpty() || vehicleOpt.isEmpty()) {
            return "User or Vehicle not found!";
        }
        Cart cart = new Cart();
        cart.setRenter(userOpt.get());
        cart.setVehicle(vehicleOpt.get());
        cartRepository.save(cart);
        return "Vehicle added to cart!";
    }

    public List<Cart> viewCart(Long userId) {
        Optional<Users> userOpt = userRepository.findById(userId);
        return userOpt.map(cartRepository::findByRenter).orElse(null);
    }

    @Transactional
    public String removeFromCart(Long userId, Long vehicleId) {
        Optional<Users> userOpt = userRepository.findById(userId);
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vehicleId);
        if (userOpt.isEmpty() || vehicleOpt.isEmpty()) {
            return "User or Vehicle not found!";
        }
        Users user = userOpt.get();
        Vehicle vehicle = vehicleOpt.get();
        Optional<Cart> cartItem = cartRepository.findByRenterAndVehicle(user, vehicle);
        if (cartItem.isPresent()) {
            cartRepository.delete(cartItem.get());
            return "Vehicle removed from cart successfully!";
        } else {
            return "Vehicle not found in cart!";
        }
    }

}
