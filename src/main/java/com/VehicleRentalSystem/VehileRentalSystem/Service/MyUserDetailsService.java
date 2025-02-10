package com.VehicleRentalSystem.VehileRentalSystem.Service;
import com.VehicleRentalSystem.VehileRentalSystem.Model.UserPrincipal;
import com.VehicleRentalSystem.VehileRentalSystem.Model.Users;
import com.VehicleRentalSystem.VehileRentalSystem.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;


@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<Users> user = userRepo.findByUserName(userName);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        Users foundUser = user.get();

        // Append "ROLE_" to roles before passing them to Spring Security
        String roleWithPrefix = "ROLE_" + foundUser.getRole().toUpperCase();

        return new org.springframework.security.core.userdetails.User(
                foundUser.getUserName(),
                foundUser.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(roleWithPrefix))
        );
    }

}