package com.VehicleRentalSystem.VehicleRentalSystem.Repository;
import com.VehicleRentalSystem.VehicleRentalSystem.Model.Cart;
import com.VehicleRentalSystem.VehicleRentalSystem.Model.Users;
import com.VehicleRentalSystem.VehicleRentalSystem.Model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByUser(Users user); // ✅ Use user instead of renter

    List<Cart> findByUser_UserId(Long userId);
    void deleteByUser_UserId(Long userId);

//    List<Cart> findByUser(Users user);

    Optional<Cart> findByUserAndVehicle(Users user, Vehicle vehicle);
}