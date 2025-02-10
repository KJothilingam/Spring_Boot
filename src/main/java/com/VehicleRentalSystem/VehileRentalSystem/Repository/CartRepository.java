package com.VehicleRentalSystem.VehileRentalSystem.Repository;
import com.VehicleRentalSystem.VehileRentalSystem.Model.Cart;
import com.VehicleRentalSystem.VehileRentalSystem.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByRenter(Users renter);
    void deleteByRenter(Users renter);
}
