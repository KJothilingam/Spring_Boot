package com.VehicleRentalSystem.VehileRentalSystem.Repository;


import com.VehicleRentalSystem.VehileRentalSystem.Model.Cart;
import com.VehicleRentalSystem.VehileRentalSystem.Model.Rental;
import com.VehicleRentalSystem.VehileRentalSystem.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByBorrower(Users users); // Remove @NotNull
    List<Rental> findByBorrowerAndIsReturnedFalse(Users borrower);
}

