package com.VehicleRentalSystem.VehileRentalSystem.Repository;
import com.VehicleRentalSystem.VehileRentalSystem.Model.Cart;
import com.VehicleRentalSystem.VehileRentalSystem.Model.Users;
import com.VehicleRentalSystem.VehileRentalSystem.Model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByRenter(Users renter);
    void deleteByRenter(Users renter);
    Optional<Cart> findByRenterAndVehicle(Users renter, Vehicle vehicle);

}
