package com.VehicleRentalSystem.VehicleRentalSystem.Model;//package com.VehicleRentalSystem.VehicleRentalSystem.Entity;

import com.VehicleRentalSystem.VehicleRentalSystem.Model.Users;
import com.VehicleRentalSystem.VehicleRentalSystem.Model.Vehicle;
import jakarta.persistence.*;
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    public Cart(Users user, Vehicle vehicle) {
        this.user = user;
        this.vehicle = vehicle;
    }

    public Cart() {}

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }


    public Long getVehicleId() {
        return vehicle != null ? vehicle.getId() : null;
    }
}
