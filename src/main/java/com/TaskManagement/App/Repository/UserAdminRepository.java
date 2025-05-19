package com.TaskManagement.App.Repository;

import com.TaskManagement.App.Model.Role;
import com.TaskManagement.App.Model.UserAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAdminRepository extends JpaRepository<UserAdmin, Long> {
    Optional<UserAdmin> findByEmail(String email);
    Optional<UserAdmin> findByRole(Role role);

}
