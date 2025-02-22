package com.VehicleRentalSystem.VehicleRentalSystem.Repository;

import com.VehicleRentalSystem.VehicleRentalSystem.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

    Optional<Users> findByUserName(String username);

}
