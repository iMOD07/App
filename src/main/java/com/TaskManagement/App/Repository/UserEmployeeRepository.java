package com.TaskManagement.App.Repository;

import com.TaskManagement.App.Model.UserEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserEmployeeRepository extends JpaRepository<UserEmployee, Long> {
    Optional<UserEmployee> findByEmail(String email);

    boolean existsByMobileNumber(String mobileNumber);
    boolean existsByEmail(String email);

}
