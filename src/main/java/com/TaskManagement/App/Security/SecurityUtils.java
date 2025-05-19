package com.TaskManagement.App.Security;

import com.TaskManagement.App.Model.UserClient;
import com.TaskManagement.App.Repository.UserClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final UserClientRepository userClientRepository;

    // ✅ استخراج العميل الحالي من Authentication
    public UserClient getCurrentClient(Authentication authentication) {
        String email = authentication.getName();
        return userClientRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Client not found for email: " + email));
    }
}
