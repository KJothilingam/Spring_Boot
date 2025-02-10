package com.VehicleRentalSystem.VehileRentalSystem.Repository;

import com.VehicleRentalSystem.VehileRentalSystem.Model.Vehicle;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {


    List<Vehicle> findByNameContainingIgnoreCase(String name, Sort sort);

    List<Vehicle> findByNumberPlateContainingIgnoreCase(String numberPlate, Sort sort);

    List<Vehicle> findByAvailableCountGreaterThanEqual(Integer availableCount, Sort sort);}