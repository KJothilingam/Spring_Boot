package com.VehicleRentalSystem.VehicleRentalSystem.Repository;

import com.VehicleRentalSystem.VehicleRentalSystem.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUserName(String userName);

    Optional<Users> findByEmail(String email);
    @Query("SELECT u FROM Users u WHERE u.role = 'RENTER'")
    List<Users> findRenters();
}
