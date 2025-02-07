package com.VehicleRentalSystem.VehileRentalSystem.Service;

import com.VehicleRentalSystem.VehileRentalSystem.Model.UserPrincipal;
import com.VehicleRentalSystem.VehileRentalSystem.Model.Users;
import com.VehicleRentalSystem.VehileRentalSystem.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<Users> user = userRepo.findByUserName(userName);
        if (user.isEmpty()) {
            System.out.println("User Not Found -jwt");
            throw new UsernameNotFoundException("User not found jwt throw");
        }
        return new UserPrincipal(user.get());

//        Users user = userRepo.findByUsername(userName);
//        if (user == null) {
//            System.out.println("User Not Found -jwt");
//            throw new UsernameNotFoundException("user not found jwt throw");
//        }
//
//        return new UserPrincipal(user);
    }
}
