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
        rental.setReturnDate(LocalDate.now().plusDays(1)); // Fix Null Issue
    }

    double additionalCost = vehicle.getRentalPrice();

    if (user.getSecurityDeposit() < additionalCost) {
        return "Insufficient security deposit! Required: ₹" + additionalCost + ", Available: ₹" + user.getSecurityDeposit();
    }

    user.setSecurityDeposit(user.getSecurityDeposit() - (int) additionalCost);
    userRepository.save(user);

    rental.setReturnDate(rental.getReturnDate().plusDays(1));
    rental.setExtensionCount(rental.getExtensionCount() + 1);

    // Update total cost
    rental.setTotalCost(rental.getTotalCost() + (int) additionalCost);

    rentalRepository.save(rental);

    return "Rental extended!\n" +
            "Extension Count: " + rental.getExtensionCount() + "\n" +
            "New Return Date: " + rental.getReturnDate() + "\n" +
            "Additional Cost: ₹" + additionalCost;

//    return "Rental extended! New Return Date: " + rental.getReturnDate() + " | Additional Cost: ₹" + additionalCost;
}


public String returnVehicle(Long rentalId, int kmsDriven, String damageLevel, String paymentMethod) {
    Rental rental = rentalRepository.findById(rentalId)
            .orElseThrow(() -> new RuntimeException("Rental not found"));
    Vehicle vehicle = rental.getVehicle();
    Users user = rental.getBorrower();

    double damageFee = calculateDamageFee(damageLevel);
    double securityDeposit = user.getSecurityDeposit();
    String paymentMessage = "";

    if ("SECURITY".equalsIgnoreCase(paymentMethod)) {
        if (securityDeposit >= damageFee) {
            user.setSecurityDeposit((int) (securityDeposit - damageFee));
            paymentMessage = "Amount deducted from Security Deposit: ₹" + damageFee +
                    ". Remaining Security Deposit: ₹" + user.getSecurityDeposit();
        } else {
            return "Insufficient security deposit. Please choose another payment method.";
        }
    } else if ("CASH".equalsIgnoreCase(paymentMethod)) {
        paymentMessage = "Amount paid by Cash: ₹" + damageFee;
    } else if ("CREDIT".equalsIgnoreCase(paymentMethod)) {
        paymentMessage = "Amount paid by Credit Card: ₹" + damageFee;
    }

    int temp=rental.getTotalCost();
    rental.setTotalCost(rental.getTotalCost() + (int) damageFee);
    rental.setReturned(true);
    rental.setReturnDate(LocalDate.now());
    vehicle.setAvailable(true);

    rentalRepository.save(rental);
    return String.format("Vehicle returned successfully.\nRental Charges: ₹%d\nDamage Charges: ₹%.2f\nTotal Cost: ₹%d\n%s",
            temp, damageFee, rental.getTotalCost(), paymentMessage);

//    return "Vehicle returned successfully. " + paymentMessage +"Rental Charges :"+temp +"Total Charges : "+rental.getTotalCost();
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

