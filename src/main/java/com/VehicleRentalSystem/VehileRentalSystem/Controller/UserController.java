package com.VehicleRentalSystem.VehileRentalSystem.Controller;

import com.VehicleRentalSystem.VehileRentalSystem.Model.Users;
import com.VehicleRentalSystem.VehileRentalSystem.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public Users register(@RequestBody Users user){
//        System.out.println(user +" Getting Added ");
        return service.registerUser(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody  Users user){
//        System.out.println("login >>>>");
         return service.verify(user);
    }


    @GetMapping("/list")
    public ResponseEntity<List<Users>> getUsers(){
        System.out.println("User List");
        return ResponseEntity.ok(service.findAll());
    }
    @GetMapping("/email/{email}")
    public ResponseEntity<Optional<Users>> getUserByEmail(@PathVariable String email) {
        System.out.println("email Search");
        return ResponseEntity.ok(service.getUserByEmail(email));
    }
}
