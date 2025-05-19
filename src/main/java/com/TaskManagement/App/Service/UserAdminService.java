package com.TaskManagement.App.Service;

import com.TaskManagement.App.Model.UserAdmin;
import com.TaskManagement.App.Repository.UserAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserAdminService {

    private final UserAdminRepository userAdminRepository;

    public UserAdmin registerAdmin(String fullName,
                                   String email,
                                   String passwordHash,
                                   String mobileNumber) {

        UserAdmin userAdmin = new UserAdmin();
        userAdmin.setFullName(fullName);
        userAdmin.setEmail(email);
        userAdmin.setPasswordHash(passwordHash);
        userAdmin.setMobileNumber(mobileNumber);
        return userAdminRepository.save(userAdmin);
    }

    public Optional<UserAdmin> findByEmail(String email) {
        return userAdminRepository.findByEmail(email);
    }

    public Optional<UserAdmin> findById(Long id) {
        return userAdminRepository.findById(id);
    }

}
