package com.TaskManagement.App;

import com.TaskManagement.App.Model.Role;
import com.TaskManagement.App.Model.UserAdmin;
import com.TaskManagement.App.Repository.UserAdminRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserAdminCreateAuto {

    private final UserAdminRepository userAdminRepository ;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (userAdminRepository.count() == 0) {
            UserAdmin userAdmin = new UserAdmin(); {
                userAdmin.setEmail("admin@admin.com");
                userAdmin.setPasswordHash(passwordEncoder.encode("Aa@102030"));
                userAdmin.setFullName("System Admin");
                userAdmin.setMobileNumber("0503369271");
                userAdmin.setRole(Role.ADMIN);
                userAdminRepository.save(userAdmin);
                System.out.println("Default system admin created / Email: admin@admin.com, Password: Aa@102030");
            }
        }
    }
}
