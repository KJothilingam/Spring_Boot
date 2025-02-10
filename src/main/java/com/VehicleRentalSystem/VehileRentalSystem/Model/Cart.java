package com.VehicleRentalSystem.VehileRentalSystem.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class Cart {
    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", renter=" + renter +
                ", vehicle=" + vehicle +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getRenter() {
        return renter;
    }

    public void setRenter(Users renter) {
        this.renter = renter;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users renter;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;
}

