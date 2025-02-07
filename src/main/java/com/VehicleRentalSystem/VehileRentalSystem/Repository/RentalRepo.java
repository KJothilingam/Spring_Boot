package com.VehicleRentalSystem.VehileRentalSystem.Repository;

import com.VehicleRentalSystem.VehileRentalSystem.Model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepo extends JpaRepository<Rental,Long> {
//    List<Rental> findAllById(Iterable<Long> longs);
}
