package com.VehicleRentalSystem.VehicleRentalSystem.Repository;

import com.VehicleRentalSystem.VehicleRentalSystem.Model.Rental;
import com.VehicleRentalSystem.VehicleRentalSystem.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findByBorrowerAndIsReturnedFalse(Users borrower);
    List<Rental> findByIsReturnedFalse();
    List<Rental> findByBorrowerUserId(Long borrowerUserId);
    List<Rental> findByBorrowerUserIdAndIsReturnedFalse(Long borrowerUserId);
    long countByVehicleId(Long id);
    List<Rental> findByBorrowerUserIdAndIsReturnedTrue(Long userId);
}

