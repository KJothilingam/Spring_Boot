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
import org.springframework.stereotype.Service;
import com.VehicleRentalSystem.VehicleRentalSystem.Model.Cart;

import java.time.LocalDate;
import java.util.List;
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

//
//    @Transactional
//    public String checkoutCart(Long userId) {
//        Optional<Users> userOpt = userRepository.findById(userId);
//        if (userOpt.isEmpty()) {
//            return "User not found!";
//        }
//        Users user = userOpt.get();
//        List<Cart> cartItems = cartRepository.findByRenter(user);
//        if (cartItems.isEmpty()) {
//            return "Cart is empty!";
//        }
//
//        List<Rental> activeRentals = rentalRepository.findByBorrowerAndIsReturnedFalse(user);
//        boolean hasBike = false;
//        boolean hasCar = false;
//        for (Rental rental : activeRentals) {
//            if (rental.getVehicle().getType().equalsIgnoreCase("BIKE")) {
//                hasBike = true;
//            } else if (rental.getVehicle().getType().equalsIgnoreCase("CAR")) {
//                hasCar = true;
//            }
//        }
//
//        int totalRentalCost = 0;
//        boolean rentingBike = false;
//        boolean rentingCar = false;
//        LocalDate returnDate = LocalDate.now().plusDays(1); // Set initial return date (1 day rental)
//
//        for (Cart cart : cartItems) {
//            Vehicle vehicle = cart.getVehicle();
//
//            if (vehicle.getAvailableCount() <= 0) {
//                return "Vehicle " + vehicle.getName() + " is not available!";
//            }
//
//            if ((vehicle.getType().equalsIgnoreCase("BIKE") && vehicle.getLastServiceAt() >= 1500) ||
//                    (vehicle.getType().equalsIgnoreCase("CAR") && vehicle.getLastServiceAt() >= 3000)) {
//                return "Vehicle " + vehicle.getName() + " needs servicing!";
//            }
//
//            if (vehicle.getType().equalsIgnoreCase("BIKE")) {
//                if (hasBike || rentingBike) {
//                    return "You can only rent one Bike at a time!";
//                }
//                rentingBike = true;
//            } else if (vehicle.getType().equalsIgnoreCase("CAR")) {
//                if (hasCar || rentingCar) {
//                    return "You can only rent one Car at a time!";
//                }
//                rentingCar = true;
//            }
//
//            totalRentalCost += vehicle.getRentalPrice();
//        }
//
//        if (user.getSecurityDeposit() < totalRentalCost) {
//            return "Insufficient security deposit! Required: ₹" + totalRentalCost + ", Available: ₹" + user.getSecurityDeposit();
//        }
//
//        user.setSecurityDeposit(user.getSecurityDeposit() - totalRentalCost);
//        userRepository.save(user);
//
//        String vehicleRent = "";
//        for (Cart cart : cartItems) {
//            Vehicle vehicle = cart.getVehicle();
//            vehicleRent += " " + vehicle;
//            vehicle.setAvailableCount(vehicle.getAvailableCount() - 1);
//            vehicleRepository.save(vehicle);
//
//            Rental rental = new Rental();
//            rental.setBorrower(user);
//            rental.setVehicle(vehicle);
//            rental.setRentalDate(LocalDate.now());
//            rental.setReturnDate(returnDate);
//            rental.setReturned(false);
//            rental.setExtensionCount(0);
//            rentalRepository.save(rental);
//        }
//        cartRepository.deleteByRenter(user);
//        return "Checkout successful! Total Rental Cost: ₹" + totalRentalCost + " | Return Date: " + returnDate + " | Rented-> " + vehicleRent;
//    }
//
    @Transactional
    public String extendRental(Long rentalId) {
        Optional<Rental> rentalOpt = rentalRepository.findById(rentalId);
        if (rentalOpt.isEmpty()) {
            return "Rental not found!";
        }
        Rental rental = rentalOpt.get();
        Users user = rental.getBorrower();
        Vehicle vehicle = rental.getVehicle();

        if (rental.isReturned()) {
            return "Cannot extend! Vehicle is already returned.";
        }

        if (rental.getExtensionCount() >= 2) {
            return "Rental extension limit reached!";
        }

        if (rental.getReturnDate() == null) {
            rental.setReturnDate(LocalDate.now().plusDays(1)); // Fixing Null Issue
        }

        double additionalCost = vehicle.getRentalPrice();

        if (user.getSecurityDeposit() < additionalCost) {
            return "Insufficient security deposit! Required: ₹" + additionalCost + ", Available: ₹" + user.getSecurityDeposit();
        }

        user.setSecurityDeposit(user.getSecurityDeposit() - (int) additionalCost);
        userRepository.save(user);

        rental.setReturnDate(rental.getReturnDate().plusDays(1));
        rental.setExtensionCount(rental.getExtensionCount() + 1);
        rental.setFineAmount(rental.getFineAmount() + additionalCost); // Add cost to total rental amount
        rentalRepository.save(rental);

        return "Rental extended! New Return Date: " + rental.getReturnDate() + " | Additional Cost: ₹" + additionalCost;
    }

//    public List<Rental> getRentalHistory(Long userId) {
//        Optional<Users> userOpt = userRepository.findById(userId);
//        return userOpt.map(rentalRepository::findByBorrower).orElse(null);
//    }
//
    @Transactional
    public String returnVehicle(Long rentalId, int kmsDriven, String damageLevel, boolean paidByCash) {
        Optional<Rental> rentalOpt = rentalRepository.findById(rentalId);
        if (rentalOpt.isEmpty()) {
            return "Rental not found!";
        }
        Rental rental = rentalOpt.get();
        Vehicle vehicle = rental.getVehicle();
        Users user = rental.getBorrower();
        if (rental.isReturned()) {
            return "Vehicle is already returned!";
        }
        int fineAmount = 0;
        int baseDeposit = 30000;
        if (kmsDriven > 500) {
            fineAmount += vehicle.getRentalPrice() * 0.15;
        }

        switch (damageLevel.toUpperCase()) {
            case "LOW":
                fineAmount += baseDeposit * 0.20; // 20% of ₹30000
                break;
            case "MEDIUM":
                fineAmount += baseDeposit * 0.50; // 50% of ₹30000
                break;
            case "HIGH":
                fineAmount += baseDeposit * 0.75; // 75% of ₹30000
                break;
            case "NONE":
                break;
            default:
                return "Invalid damage level! Choose LOW, MEDIUM, HIGH, or NONE.";
        }
        if (!paidByCash) {
            if (user.getSecurityDeposit() < fineAmount) {
                return "Insufficient security deposit! Fine amount: ₹" + fineAmount;
            }
            user.setSecurityDeposit(user.getSecurityDeposit() - fineAmount);
        }
        userRepository.save(user);

        rental.setReturned(true);
        rental.setFineAmount(fineAmount);
        rental.setReturnDate(LocalDate.now());
        rentalRepository.save(rental);

        vehicle.setTotalKmsDriven(vehicle.getTotalKmsDriven() + kmsDriven);
        if ((vehicle.getType().equalsIgnoreCase("BIKE") && vehicle.getTotalKmsDriven() >= 1500) ||
                (vehicle.getType().equalsIgnoreCase("CAR") && vehicle.getTotalKmsDriven() >= 3000)) {
            vehicle.setNeedsService(true);
        }

        vehicle.setAvailable(true);
        vehicleRepository.save(vehicle);
        return "Vehicle returned successfully! Fine Amount: ₹" + fineAmount +
                " | Remaining Security Deposit: ₹" + user.getSecurityDeposit();
    }
//
//    @Transactional
//    public List<Rental> getAllRentedVehicles() {
//        return rentalRepository.findAll();
//    }
//
//    @Transactional
//    public List<Rental> getUnreturnedVehicles() {
//        return rentalRepository.findByIsReturnedFalse();
//    }


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

        // 🚫 Check if user already has an active rental of the same type
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

        // 🚫 Check availability
        if (!vehicle.getAvailable()) {
            return "❌ Vehicle " + vehicle.getName() + " is not available!";
        }

        // 🚫 Check security deposit
        int requiredDeposit = vehicle.getType().equalsIgnoreCase("BIKE") ? 3000 : 10000;
        if (user.getSecurityDeposit() < requiredDeposit) {
            return "❌ Insufficient security deposit! Required: ₹" + requiredDeposit;
        }

        // ✅ Deduct deposit & rent vehicle
        user.setSecurityDeposit(user.getSecurityDeposit() - (int) vehicle.getRentalPrice());
        userRepository.save(user);

        vehicle.setAvailable(false); // Mark as unavailable
        vehicleRepository.save(vehicle);

        Rental rental = new Rental();
        rental.setBorrower(user);
        rental.setVehicle(vehicle);
        rental.setRentalDate(LocalDate.now());
        rental.setReturnDate(LocalDate.now().plusDays(1)); // One-day rental
        rental.setReturned(false);
        rental.setExtensionCount(0);
        rentalRepository.save(rental);

        // ✅ Only remove from cart if rental is successful
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

//public List<Rental> getRentalsByUserId(Long userId) {
//    return rentalRepository.findByBorrowerUserId(userId);
//}




}
