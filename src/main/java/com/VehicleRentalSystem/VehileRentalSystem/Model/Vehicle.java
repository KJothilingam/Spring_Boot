package com.VehicleRentalSystem.VehileRentalSystem.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int availableCount;
    private String name;
    private boolean needsService;
    private String numberPlate;
    private double rentalPrice;
    private int totalKmsDriven;
    private String type;
    private int lastServiceAt;








}
