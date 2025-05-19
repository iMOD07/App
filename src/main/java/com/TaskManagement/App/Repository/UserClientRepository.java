package com.TaskManagement.App.Repository;

import com.TaskManagement.App.Model.UserClient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserClientRepository extends JpaRepository<UserClient, Long> {
    Optional<UserClient> findByEmail(String email);

    boolean existsByMobileNumber(String mobileNumber);
    boolean existsByEmail(String email);
}
