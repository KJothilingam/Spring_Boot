//package com.VehicleRentalSystem.VehicleRentalSystem.Service;
//import com.VehicleRentalSystem.VehicleRentalSystem.Model.Users;
//import com.VehicleRentalSystem.VehicleRentalSystem.Repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Collections;
//import java.util.Optional;
//
//
//@Service
//public class MyUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private UserRepository userRepo;
//
//
//    @Override
//    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//        Optional<Users> user = userRepo.findByUserName(userName);
//        if (user.isEmpty()) {
//            throw new UsernameNotFoundException("User not found");
//        }
//        Users foundUser = user.get();
//        String roleWithPrefix = "ROLE_" + foundUser.getRole().toUpperCase();
//        return new org.springframework.security.core.userdetails.User(
//                foundUser.getUserName(),
//                foundUser.getPassword(),
//                Collections.singletonList(new SimpleGrantedAuthority(roleWithPrefix))
//        );
//    }
//
//}