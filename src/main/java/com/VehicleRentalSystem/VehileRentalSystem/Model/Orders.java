package com.VehicleRentalSystem.VehileRentalSystem.Model;

import jakarta.persistence.*;
import lombok.*;
import com.VehicleRentalSystem.VehileRentalSystem.Model.Users;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    private int depositPaid;
}
