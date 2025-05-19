package com.TaskManagement.App.Security;

import com.TaskManagement.App.Model.UserAdmin;
import com.TaskManagement.App.Model.UserClient;
import com.TaskManagement.App.Model.UserEmployee;
import com.TaskManagement.App.Repository.UserAdminRepository;
import com.TaskManagement.App.Repository.UserClientRepository;
import com.TaskManagement.App.Repository.UserEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAdminRepository adminRepo;
    private final UserClientRepository clientRepo;
    private final UserEmployeeRepository employeeRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        if (adminRepo.findByEmail(email).isPresent()) {
            UserAdmin admin = adminRepo.findByEmail(email).get();
            return new User(admin.getEmail(), admin.getPasswordHash(),
                    java.util.List.of(() -> "ROLE_ADMIN"));
        }

        if (clientRepo.findByEmail(email).isPresent()) {
            UserClient client = clientRepo.findByEmail(email).get();
            return new User(client.getEmail(), client.getPasswordHash(),
                    java.util.List.of(() -> "ROLE_CLIENT"));
        }

        if (employeeRepo.findByEmail(email).isPresent()) {
            UserEmployee emp = employeeRepo.findByEmail(email).get();
            return new User(emp.getEmail(), emp.getPasswordHash(),
                    java.util.List.of(() -> "ROLE_EMPLOYEE"));
        }

        throw new UsernameNotFoundException("User not found: " + email);
    }
}
