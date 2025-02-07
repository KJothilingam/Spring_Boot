package com.VehicleRentalSystem.VehileRentalSystem.Repository;

import com.VehicleRentalSystem.VehileRentalSystem.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);

//    Users findByUserName(String userName);

    Optional<Users> findByUserName(String username);

//    Optional<Users> findByUsername(String username);

//    @Query("SELECT u FROM Users u WHERE LOWER(u.userName) = LOWER(:userName)")
//    Users findByUsername(@Param("userName") String userName);
}
