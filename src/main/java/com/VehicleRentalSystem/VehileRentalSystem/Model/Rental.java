package com.VehicleRentalSystem.VehileRentalSystem.Model;

import jakarta.persistence.*;
import com.VehicleRentalSystem.VehileRentalSystem.Model.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "rentals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Users borrower;

    @ManyToOne
    private Vehicle vehicle;

    private LocalDate rentalDate;
    private LocalDate returnDate;
    private double fineAmount;
    private boolean isReturned;
}
