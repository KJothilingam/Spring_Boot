package com.VehicleRentalSystem.VehileRentalSystem.Repository;


import com.VehicleRentalSystem.VehileRentalSystem.Model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
}
