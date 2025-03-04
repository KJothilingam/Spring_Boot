package com.VehicleRentalSystem.VehicleRentalSystem.Controller;

import com.VehicleRentalSystem.VehicleRentalSystem.Model.Users;
import com.VehicleRentalSystem.VehicleRentalSystem.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService service;

//    @PostMapping("/register")
//    public Users register(@RequestBody Users user){
//        System.out.println(user +" Getting Added ");
////        return service.registerUser(user);
//    }

//    @PostMapping("/login")
//    public String login(@RequestBody  Users user){
////        System.out.println("login >>>>");
//         return service.verify(user);
//    }

@PostMapping("/register")
public ResponseEntity<Map<String, String>> register(@RequestBody Users user) {
    String result = service.registerUser(user);

    Map<String, String> response = new HashMap<>();
    response.put("message", result);

    return ResponseEntity.ok(response);
}

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String email, @RequestParam String password) {
        Map<String, Object> response = new HashMap<>();
        Users user = service.loginUser(email, password); // ✅ Call service method

        if (user != null) {
            response.put("authenticated", true);
            response.put("userId", user.getUserId());  // ✅ Include userId in response
            response.put("role", user.getRole());
            response.put("userName", user.getUserName());
        } else {
            response.put("authenticated", false);
            response.put("userId", null);
            response.put("role", null);
            response.put("userName", null);
        }

        return ResponseEntity.ok(response);
    }


    @GetMapping("/renters")
    public List<Users> getRenters() {
        return service.getRenters();
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

//    @PreAuthorize("hasRole('ADMIN')")
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        service.deleteUserById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        return ResponseEntity.ok(response);
    }



    @PutMapping("/update/{id}")  // New API: http://localhost:8080/users/update/{id}
    public ResponseEntity<Users> updateUser(@PathVariable Long id, @RequestBody Users updatedUser) {
        Users updated = service.updateUser(id, updatedUser);
        return ResponseEntity.ok(updated);
    }


    @GetMapping("/id/{userId}")
    public ResponseEntity<Users> getUserById(@PathVariable Long userId) {
        Optional<Users> user = service.getUserById(userId);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }




}
