package com.VehicleRentalSystem.VehicleRentalSystem.Service;

import com.VehicleRentalSystem.VehicleRentalSystem.Model.Rental;
import com.VehicleRentalSystem.VehicleRentalSystem.Model.Users;
import com.VehicleRentalSystem.VehicleRentalSystem.Model.Vehicle;
import com.VehicleRentalSystem.VehicleRentalSystem.Repository.CartRepository;
import com.VehicleRentalSystem.VehicleRentalSystem.Repository.UserRepository;
import com.VehicleRentalSystem.VehicleRentalSystem.Repository.RentalRepository;
import com.VehicleRentalSystem.VehicleRentalSystem.Repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.VehicleRentalSystem.VehicleRentalSystem.Model.Cart;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private UserRepository userRepository;



    @Transactional
    public Rental extendRental(Long rentalId) {
        Optional<Rental> rentalOpt = rentalRepository.findById(rentalId);
        if (rentalOpt.isEmpty()) {
            throw new RuntimeException("Rental not found!");
        }
        Rental rental = rentalOpt.get();
        Users user = rental.getBorrower();
        Vehicle vehicle = rental.getVehicle();

        if (rental.isReturned()) {
            throw new RuntimeException("Cannot extend! Vehicle is already returned.");
        }

        if (rental.getExtensionCount() >= 2) {
            throw new RuntimeException("Rental extension limit reached!");
        }

        if (rental.getReturnDate() == null) {
            rental.setReturnDate(LocalDate.now().plusDays(1));
        }

        double additionalCost = vehicle.getRentalPrice();

        if (user.getSecurityDeposit() < additionalCost) {
            throw new RuntimeException("Insufficient security deposit! Required: ₹" + additionalCost + ", Available: ₹" + user.getSecurityDeposit());
        }

        user.setSecurityDeposit(user.getSecurityDeposit() - (int) additionalCost);
        userRepository.save(user);

        rental.setReturnDate(rental.getReturnDate().plusDays(1));
        rental.setExtensionCount(rental.getExtensionCount() + 1);
        rental.setTotalCost(rental.getTotalCost() + (int) additionalCost);

        rentalRepository.save(rental);

        return rental;
    }


    public ResponseEntity<Map<String, Object>> returnVehicle(Long rentalId, int kmsDriven, String damageLevel, String paymentMethod) {
        Map<String, Object> response = new HashMap<>();

        try {
            Rental rental = rentalRepository.findById(rentalId)
                    .orElseThrow(() -> new RuntimeException("Rental not found"));
            Vehicle vehicle = rental.getVehicle();
            Users user = rental.getBorrower();

            double damageFee = calculateDamageFee(damageLevel);
            double securityDeposit = user.getSecurityDeposit();
            String paymentMessage = "";
            double remainingSecurityDeposit = securityDeposit; // Store balance before deductions

            if ("SECURITY".equalsIgnoreCase(paymentMethod)) {
                if (securityDeposit >= damageFee) {
                    remainingSecurityDeposit -= damageFee;
                    user.setSecurityDeposit((int) remainingSecurityDeposit);
                    paymentMessage = "✅ Amount deducted from Security Deposit: ₹" + damageFee +
                            ". Remaining Security Deposit: ₹" + remainingSecurityDeposit;
                } else {
                    response.put("error", "❌ Insufficient security deposit! Please choose another payment method.");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
            } else if ("CASH".equalsIgnoreCase(paymentMethod)) {
                paymentMessage = "✅ Amount paid by Cash: ₹" + damageFee;
            } else if ("CREDIT".equalsIgnoreCase(paymentMethod)) {
                paymentMessage = "✅ Amount paid by Credit Card: ₹" + damageFee;
            }

            int previousTotalCost = rental.getTotalCost();
            rental.setTotalCost(previousTotalCost + (int) damageFee);
            rental.setReturned(true);
            rental.setReturnDate(LocalDate.now());
            vehicle.setAvailable(true);

            rentalRepository.save(rental);

            response.put("rental", rental);
            response.put("message", String.format(
                    "✅ Vehicle returned successfully.\n" +
                            "📅 Rental Charges: ₹%d\n" +
                            "💰 Damage Charges: ₹%.2f\n" +
                            "💵 Total Cost: ₹%d\n" +
                            "%s\n" +
                            "💳 Remaining Security Deposit: ₹%.2f", // Added this line
                    previousTotalCost, damageFee, rental.getTotalCost(), paymentMessage, remainingSecurityDeposit
            ));

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            System.err.println("Error in returnVehicle(): " + e.getMessage());
            response.put("error", "⚠️ An error occurred while returning the vehicle. Please try again.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    private double calculateDamageFee(String damageLevel) {
        switch (damageLevel) {
            case "LOW": return 6000;
            case "MEDIUM": return 15000;
            case "HIGH": return 22500;
            default: return 0;
        }
    }

    @Transactional
    public String rentVehicle(Long userId, Long vehicleId) {
        Optional<Users> userOpt = userRepository.findById(userId);
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vehicleId);

        if (userOpt.isEmpty() || vehicleOpt.isEmpty()) {
            return "❌ User or Vehicle not found!";
        }

        Users user = userOpt.get();
        Vehicle vehicle = vehicleOpt.get();

        Optional<Cart> cartOpt = cartRepository.findByUserAndVehicle(user, vehicle);
        if (cartOpt.isEmpty()) {
            return "❌ Vehicle not found in cart!";
        }


        List<Rental> activeRentals = rentalRepository.findByBorrowerAndIsReturnedFalse(user);
        boolean hasBike = false, hasCar = false;

        for (Rental rental : activeRentals) {
            if (rental.getVehicle().getType().equalsIgnoreCase("BIKE")) {
                hasBike = true;
            } else if (rental.getVehicle().getType().equalsIgnoreCase("CAR")) {
                hasCar = true;
            }
        }

        if (vehicle.getType().equalsIgnoreCase("BIKE") && hasBike) {
            return "❌ You can only rent one bike at a time!";
        }
        if (vehicle.getType().equalsIgnoreCase("CAR") && hasCar) {
            return "❌ You can only rent one car at a time!";
        }

        if (!vehicle.getAvailable()) {
            return "❌ Vehicle " + vehicle.getName() + " is not available!";
        }

        int requiredDeposit = vehicle.getType().equalsIgnoreCase("BIKE") ? 3000 : 10000;
        if (user.getSecurityDeposit() < requiredDeposit) {
            return "❌ Insufficient security deposit! Required: ₹" + requiredDeposit;
        }

        user.setSecurityDeposit(user.getSecurityDeposit() - (int) vehicle.getRentalPrice());

        userRepository.save(user);

        vehicle.setAvailable(false); // Mark as unavailable
        vehicleRepository.save(vehicle);

        Rental rental = new Rental();
        rental.setTotalCost(rental.getTotalCost() + (int)vehicle.getRentalPrice());
        rental.setBorrower(user);
        rental.setVehicle(vehicle);
        rental.setRentalDate(LocalDate.now());
        rental.setReturnDate(LocalDate.now().plusDays(1)); // One-day rental
        rental.setReturned(false);
        rental.setExtensionCount(0);
        rentalRepository.save(rental);

        cartRepository.delete(cartOpt.get());

        return "✅ Vehicle rented successfully! Rental Cost: ₹" + vehicle.getRentalPrice() +
                " | Return Date: " + rental.getReturnDate();
    }

    public List<Rental> getAllOrders() {
        return rentalRepository.findByIsReturnedFalse();
    }

    public List<Rental> getAllOrdersAdmin() {
        return rentalRepository.findAll();
    }
    public List<Rental> getUserRentals(Long userId) {
        return rentalRepository.findByBorrowerUserId(userId);
    }
    public List<Rental> getUserActiveRentals(Long userId) {
        return rentalRepository.findByBorrowerUserIdAndIsReturnedFalse(userId);
    }

    public List<Rental> getUserHistory(Long userId) {
        return rentalRepository.findByBorrowerUserIdAndIsReturnedTrue(userId);
    }

}

