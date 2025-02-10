package com.VehicleRentalSystem.VehileRentalSystem.Service;

import com.VehicleRentalSystem.VehileRentalSystem.Model.Users;
import com.VehicleRentalSystem.VehileRentalSystem.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private  UserRepository repository;
    private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);

    public Users registerUser(Users user) {
        user.setPassword(encoder.encode(user.getPassword())); // Encrypt password
          System.out.println("Saving user: " + user);
        return repository.save(user);
    }

    public  Optional<Users> getUserByEmail(String email) {
        return repository.findByEmail(email);
    }


public String verify(Users user) {
    System.out.println("User trying to log in: " + user.getUserName()); // Debugging

    Optional<Users> existingUser = repository.findByUserName(user.getUserName());

    if (existingUser.isEmpty()) {
        return "User not found";
    }

    // Check hashed password
    if (!encoder.matches(user.getPassword(), existingUser.get().getPassword())) {
        return "Invalid credentials";
    }

    // Authenticate user
    Authentication authentication = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));

    if (authentication.isAuthenticated()) {
        return jwtService.generateToken(user.getUserName());
    } else {
        return "Authentication failed";
    }
}

    public List<Users> findAll() {
        return repository.findAll();
    }
}
