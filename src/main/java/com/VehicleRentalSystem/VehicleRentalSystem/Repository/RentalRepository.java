package com.VehicleRentalSystem.VehicleRentalSystem.Repository;

import com.VehicleRentalSystem.VehicleRentalSystem.Model.Rental;
import com.VehicleRentalSystem.VehicleRentalSystem.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {


    List<Rental> findByBorrower(Users borrower);

    List<Rental> findByBorrowerAndIsReturnedFalse(Users borrower);

    List<Rental> findByIsReturnedFalse();

    List<Rental> findByBorrowerUserId(Long borrowerUserId);

//    List<Rental> findByUserId( borrower_userId);

//    List<Rental> findByBorrowerUserId(Long borrowerUserId);
}

