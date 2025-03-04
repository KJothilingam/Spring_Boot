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
//    @Autowired
//    private JWTService jwtService;

//    @Autowired
//    AuthenticationManager authManager;

    @Autowired
    private  UserRepository repository;

//    private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);
//
//    public Users registerUser(Users user) {
//        user.setPassword(encoder.encode(user.getPassword()));
//          System.out.println("Saving user: " + user);
//        return repository.save(user);
//    }

    public  Optional<Users> getUserByEmail(String email) {
        return repository.findByEmail(email);
    }


//public String verify(Users user) {
//    System.out.println("User trying to log in: " + user.getUserName());
//
//    Optional<Users> existingUser = repository.findByUserName(user.getUserName());
//
//    if (existingUser.isEmpty()) {
//        return "User not found";
//    }
//
//    // Check hashed password
//    if (!encoder.matches(user.getPassword(), existingUser.get().getPassword())) {
//        return "Invalid credentials";
//    }
//
//    // Authenticate user
//    Authentication authentication = authManager.authenticate(
//            new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
//
//    if (authentication.isAuthenticated()) {
//        return jwtService.generateToken(user.getUserName());
//    } else {
//        return "Authentication failed";
//    }
//}

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


//    public String registerUser(String email, String password, String phoneNo, String role, int securityDeposit, String userName) {
//        Optional<Users> existingUser = repository.findByEmail(email);
//
//        if (existingUser.isPresent()) {
//            return "Email already exists!";
//        }
//
//        Users newUser = new Users(); // Corrected from `User` to `Users`
//        newUser.setEmail(email);
//        newUser.setPassword(password); // Storing password as plain text
//        newUser.setPhoneNo(phoneNo);
//        newUser.setRole(role);
//        newUser.setSecurityDeposit(securityDeposit);
//        newUser.setUsername(userName);
//
//        repository.save(newUser);
//        return "User registered successfully!";
//    }

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


}
