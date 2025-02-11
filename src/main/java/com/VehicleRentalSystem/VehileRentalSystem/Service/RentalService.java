package com.VehicleRentalSystem.VehileRentalSystem.Service;

import com.VehicleRentalSystem.VehileRentalSystem.Model.Rental;
import com.VehicleRentalSystem.VehileRentalSystem.Model.Users;
import com.VehicleRentalSystem.VehileRentalSystem.Model.Vehicle;
import com.VehicleRentalSystem.VehileRentalSystem.Repository.CartRepository;
import com.VehicleRentalSystem.VehileRentalSystem.Repository.UserRepository;
import com.VehicleRentalSystem.VehileRentalSystem.Repository.RentalRepository;
import com.VehicleRentalSystem.VehileRentalSystem.Repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.VehicleRentalSystem.VehileRentalSystem.Model.Cart;

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


    @Transactional
    public String checkoutCart(Long userId) {
        Optional<Users> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return "User not found!";
        }
        Users user = userOpt.get();
        List<Cart> cartItems = cartRepository.findByRenter(user);
        if (cartItems.isEmpty()) {
            return "Cart is empty!";
        }

        // Fetch Active Rentals (Check if user already rented a vehicle)
        List<Rental> activeRentals = rentalRepository.findByBorrowerAndIsReturnedFalse(user);
        boolean hasBike = false;
        boolean hasCar = false;
        for (Rental rental : activeRentals) {
            if (rental.getVehicle().getType().equalsIgnoreCase("BIKE")) {
                hasBike = true;
            } else if (rental.getVehicle().getType().equalsIgnoreCase("CAR")) {
                hasCar = true;
            }
        }

        int totalRentalCost = 0;
        boolean rentingBike = false;
        boolean rentingCar = false;
        LocalDate returnDate = LocalDate.now().plusDays(1); // Set initial return date (1 day rental)

        for (Cart cart : cartItems) {
            Vehicle vehicle = cart.getVehicle();

            // 🚗 **Check if the vehicle is available**
            if (vehicle.getAvailableCount() <= 0) {
                return "Vehicle " + vehicle.getName() + " is not available!";
            }

            // 🚗 **Check Service Requirement**
            if ((vehicle.getType().equalsIgnoreCase("BIKE") && vehicle.getLastServiceAt() >= 1500) ||
                    (vehicle.getType().equalsIgnoreCase("CAR") && vehicle.getLastServiceAt() >= 3000)) {
                return "Vehicle " + vehicle.getName() + " needs servicing!";
            }

            // 🚗 **Check if the user is trying to rent more than allowed**
            if (vehicle.getType().equalsIgnoreCase("BIKE")) {
                if (hasBike || rentingBike) {
                    return "You can only rent one Bike at a time!";
                }
                rentingBike = true;
            } else if (vehicle.getType().equalsIgnoreCase("CAR")) {
                if (hasCar || rentingCar) {
                    return "You can only rent one Car at a time!";
                }
                rentingCar = true;
            }

            // 🚗 **Calculate Total Rental Cost**
            totalRentalCost += vehicle.getRentalPrice();
        }

        // 🚗 **Check if user has enough security deposit**
        if (user.getSecurityDeposit() < totalRentalCost) {
            return "Insufficient security deposit! Required: ₹" + totalRentalCost + ", Available: ₹" + user.getSecurityDeposit();
        }

        // 🚗 **Deduct the rental price from security deposit**
        user.setSecurityDeposit(user.getSecurityDeposit() - totalRentalCost);
        userRepository.save(user);

        String vehicleRent = "";
        for (Cart cart : cartItems) {
            Vehicle vehicle = cart.getVehicle();
            vehicleRent += " " + vehicle;
            vehicle.setAvailableCount(vehicle.getAvailableCount() - 1);
            vehicleRepository.save(vehicle);

            // 🚗 **Create Rental Record**
            Rental rental = new Rental();
            rental.setBorrower(user);
            rental.setVehicle(vehicle);
            rental.setRentalDate(LocalDate.now());
            rental.setReturnDate(returnDate); // ✅ Ensure return date is set
            rental.setReturned(false);
            rental.setExtensionCount(0);
            rentalRepository.save(rental);
        }

        // 🚗 **Clear cart after checkout**
        cartRepository.deleteByRenter(user);
        return "Checkout successful! Total Rental Cost: ₹" + totalRentalCost + " | Return Date: " + returnDate + " | Rented-> " + vehicleRent;
    }

    @Transactional
    public String extendRental(Long rentalId) {
        Optional<Rental> rentalOpt = rentalRepository.findById(rentalId);

        if (rentalOpt.isEmpty()) {
            return "Rental not found!";
        }

        Rental rental = rentalOpt.get();
        Users user = rental.getBorrower();
        Vehicle vehicle = rental.getVehicle();

        // 🚗 **Check if the rental is already returned**
        if (rental.isReturned()) {
            return "Cannot extend! Vehicle is already returned.";
        }

        // 🚗 **Check if the rental can be extended (Max: 2 times)**
        if (rental.getExtensionCount() >= 2) {
            return "Rental extension limit reached!";
        }

        // 🚗 **Check if return date is null and set a default value**
        if (rental.getReturnDate() == null) {
            rental.setReturnDate(LocalDate.now().plusDays(1)); // Fixing Null Issue
        }

        // 🚗 **Calculate Additional Cost**
        double additionalCost = vehicle.getRentalPrice();

        // 🚗 **Check if user has enough security deposit**
        if (user.getSecurityDeposit() < additionalCost) {
            return "Insufficient security deposit! Required: ₹" + additionalCost + ", Available: ₹" + user.getSecurityDeposit();
        }

        // 🚗 **Deduct additional rental cost from security deposit**
        user.setSecurityDeposit(user.getSecurityDeposit() - (int) additionalCost);
        userRepository.save(user);

        // 🚗 **Extend Return Date & Update Cost**
        rental.setReturnDate(rental.getReturnDate().plusDays(1));
        rental.setExtensionCount(rental.getExtensionCount() + 1);
        rental.setFineAmount(rental.getFineAmount() + additionalCost); // Add cost to total rental amount
        rentalRepository.save(rental);

        return "Rental extended! New Return Date: " + rental.getReturnDate() + " | Additional Cost: ₹" + additionalCost;
    }

    public List<Rental> getRentalHistory(Long userId) {
        Optional<Users> userOpt = userRepository.findById(userId);
        return userOpt.map(rentalRepository::findByBorrower).orElse(null);
    }

//    @Transactional
//    public String returnVehicle(Long rentalId, int kmsDriven, String damageLevel, boolean paidByCash) {
//        Optional<Rental> rentalOpt = rentalRepository.findById(rentalId);
//
//        if (rentalOpt.isEmpty()) {
//            return "Rental not found!";
//        }
//
//        Rental rental = rentalOpt.get();
//        Vehicle vehicle = rental.getVehicle();
//        Users user = rental.getBorrower();
//
//        if (rental.isReturned()) {
//            return "Vehicle is already returned!";
//        }
//
//        int fineAmount = 0;
//
//        // 🚗 **Fine for exceeding 500 KM per day**
//        if (kmsDriven > 500) {
//            fineAmount += vehicle.getRentalPrice() * 0.15;
//        }
//
//        // 🚗 **Fine based on Damage Level**
//        switch (damageLevel.toUpperCase()) {
//            case "LOW":
//                fineAmount += vehicle.getRentalPrice() * 0.20;
//                break;
//            case "MEDIUM":
//                fineAmount += vehicle.getRentalPrice() * 0.50;
//                break;
//            case "HIGH":
//                fineAmount += vehicle.getRentalPrice() * 0.75;
//                break;
//            case "NONE":
//                break;
//            default:
//                return "Invalid damage level! Choose LOW, MEDIUM, HIGH, or NONE.";
//        }
//
//        // 🚗 **Deduct Fine from Security Deposit if unpaid**
//        if (!paidByCash) {
//            if (user.getSecurityDeposit() < fineAmount) {
//                return "Insufficient security deposit! Fine amount: ₹" + fineAmount;
//            }
//            user.setSecurityDeposit(user.getSecurityDeposit() - fineAmount);
//            userRepository.save(user);
//        }
//
//        // 🚗 **Mark Vehicle as Returned**
//        rental.setReturned(true);
//        rental.setFineAmount(fineAmount);
//        rental.setReturnDate(LocalDate.now());
//        rentalRepository.save(rental);
//
//        // 🚗 **Update Vehicle KMs and Check for Service**
//        vehicle.setTotalKmsDriven(vehicle.getTotalKmsDriven() + kmsDriven);
//        if ((vehicle.getType().equalsIgnoreCase("BIKE") && vehicle.getTotalKmsDriven() >= 1500) ||
//                (vehicle.getType().equalsIgnoreCase("CAR") && vehicle.getTotalKmsDriven() >= 3000)) {
//            vehicle.setNeedsService(true);
//        }
//
//        // 🚗 **Increase Vehicle Availability**
//        vehicle.setAvailableCount(vehicle.getAvailableCount() + 1);
//        vehicleRepository.save(vehicle);
//
//        return "Vehicle returned successfully! Fine Amount: ₹" + fineAmount;
//    }
}
