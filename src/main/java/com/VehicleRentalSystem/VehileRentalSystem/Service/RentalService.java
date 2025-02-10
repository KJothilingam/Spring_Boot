package com.VehicleRentalSystem.VehileRentalSystem.Service;

import com.VehicleRentalSystem.VehileRentalSystem.Model.Rental;
import com.VehicleRentalSystem.VehileRentalSystem.Model.Users;
import com.VehicleRentalSystem.VehileRentalSystem.Repository.UserRepository;
import com.VehicleRentalSystem.VehileRentalSystem.Repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Rental> getRentalHistory(Long userId) {
        Optional<Users> userOpt = userRepository.findById(userId);
        return userOpt.map(rentalRepository::findByBorrower).orElse(null);
    }
}
