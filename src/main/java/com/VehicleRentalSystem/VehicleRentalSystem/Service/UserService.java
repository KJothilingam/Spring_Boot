package com.VehicleRentalSystem.VehicleRentalSystem.Service;

import com.VehicleRentalSystem.VehicleRentalSystem.Model.Users;
import com.VehicleRentalSystem.VehicleRentalSystem.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private  UserRepository repository;

    public  Optional<Users> getUserByEmail(String email) {
        return repository.findByEmail(email);
    }

    public List<Users> findAll() {
        return repository.findAll();
    }


    public boolean updateSecurityDeposit(Long userId, int securityDeposit) {
        Optional<Users> optionalUser = repository.findById(userId);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            if ("ADMIN".equals(user.getRole())) {
                return false;
            }
            user.setSecurityDeposit(securityDeposit);
            repository.save(user);
            return true;
        }
        return false;
    }

    public String registerUser(Users user) {
        Optional<Users> existingUser = repository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            return "Email already exists!";
        }

        // Set default values if not provided
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("RENTER"); // Default role
        }
        user.setSecurityDeposit(0); // Always default to 0

        repository.save(user);
        return "User registered successfully!";
    }

    public List<Users> getRenters() {
        return repository.findRenters();
    }

    public Users updateUser(Long id, Users updatedUser) {
        Optional<Users> existingUserOpt = repository.findById(id);
        if (existingUserOpt.isPresent()) {
            Users existingUser = existingUserOpt.get();

            // Only update non-null fields
            if (updatedUser.getUserName() != null) {
                existingUser.setUserName(updatedUser.getUserName());
            }
            if (updatedUser.getPhoneNo() != null) {
                existingUser.setPhoneNo(updatedUser.getPhoneNo());
            }
            if (updatedUser.getSecurityDeposit() != 0) {  // Assumes 0 means "no update"
                existingUser.setSecurityDeposit(updatedUser.getSecurityDeposit());
            }


            return repository.save(existingUser);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public Users loginUser(String email, String password) {
        Optional<Users> userOpt = repository.findByEmail(email);

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return userOpt.get(); // ✅ Return user if credentials match
        }

        return null; // ✅ Return null if authentication fails
    }

    public void deleteUserById(Long id) {
        Optional<Users> user = repository.findById(id);
        if (user.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public Optional<Users> getUserById(Long userId) {
        return repository.findById(userId);
    }

    public Users updateUserProfile(Long userId, Users updatedUser) {
        return repository.findById(userId).map(user -> {
            user.setUserName(updatedUser.getUserName());  // ✅ Corrected method name
            user.setPhoneNo(updatedUser.getPhoneNo());    // ✅ No issue
            user.setEmail(updatedUser.getEmail());        // ✅ No issue
            return repository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }


    public double getUserSecurityDeposit(Long userId) {
        return repository.getById(userId).getSecurityDeposit();
    }
}
