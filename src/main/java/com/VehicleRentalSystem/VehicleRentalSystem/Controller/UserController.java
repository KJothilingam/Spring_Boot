package com.VehicleRentalSystem.VehicleRentalSystem.Controller;

import com.VehicleRentalSystem.VehicleRentalSystem.Model.Users;
import com.VehicleRentalSystem.VehicleRentalSystem.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
        System.out.println(user +" Getting Added ");
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

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-deposit/{userId}")
    public ResponseEntity<String> updateSecurityDeposit(
            @PathVariable Long userId,
            @RequestParam int securityDeposit) {
        boolean updated = service.updateSecurityDeposit(userId, securityDeposit);
        if (updated) {
            return ResponseEntity.ok("Security deposit updated successfully!");
        } else {
            return ResponseEntity.badRequest().body("User not found or update not allowed!");
        }
    }


}
