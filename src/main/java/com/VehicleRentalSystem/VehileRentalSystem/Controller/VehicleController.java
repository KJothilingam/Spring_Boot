package com.VehicleRentalSystem.VehileRentalSystem.Controller;

import com.VehicleRentalSystem.VehileRentalSystem.Model.Users;
import com.VehicleRentalSystem.VehileRentalSystem.Model.Vehicle;
import com.VehicleRentalSystem.VehileRentalSystem.Service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService service;

    @GetMapping("/list")
    public ResponseEntity<List<Vehicle>> getUsers(){
        System.out.println("Vehicle List");
        return ResponseEntity.ok(service.findAll());
    }
}
