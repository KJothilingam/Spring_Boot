package com.VehicleRentalSystem.VehicleRentalSystem.Model;


import jakarta.persistence.*;

@Entity
@Table(name = "vehicles")
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

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", availableCount=" + availableCount +
                ", name='" + name + '\'' +
                ", needsService=" + needsService +
                ", numberPlate='" + numberPlate + '\'' +
                ", rentalPrice=" + rentalPrice +
                ", totalKmsDriven=" + totalKmsDriven +
                ", type='" + type + '\'' +
                ", lastServiceAt=" + lastServiceAt +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(int availableCount) {
        this.availableCount = availableCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNeedsService() {
        return needsService;
    }

    public void setNeedsService(boolean needsService) {
        this.needsService = needsService;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public double getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(double rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public int getTotalKmsDriven() {
        return totalKmsDriven;
    }

    public void setTotalKmsDriven(int totalKmsDriven) {
        this.totalKmsDriven = totalKmsDriven;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLastServiceAt() {
        return lastServiceAt;
    }

    public void setLastServiceAt(int lastServiceAt) {
        this.lastServiceAt = lastServiceAt;
    }
}
