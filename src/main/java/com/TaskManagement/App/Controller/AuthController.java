package com.TaskManagement.App.Controller;

import com.TaskManagement.App.Dto.AuthResponse;
import com.TaskManagement.App.Dto.ClientRegisterRequest;
import com.TaskManagement.App.Dto.EmployeeRegisterRequest;
import com.TaskManagement.App.Dto.LoginRequest;
import com.TaskManagement.App.Model.UserAdmin;
import com.TaskManagement.App.Model.UserClient;
import com.TaskManagement.App.Model.UserEmployee;
import com.TaskManagement.App.Repository.UserAdminRepository;
import com.TaskManagement.App.Repository.UserClientRepository;
import com.TaskManagement.App.Repository.UserEmployeeRepository;
import com.TaskManagement.App.Security.JwtUtil;
import com.TaskManagement.App.Service.UserClientService;
import com.TaskManagement.App.Service.UserEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final UserClientRepository userClientRepository;
    private final UserAdminRepository userAdminRepository;
    private final UserEmployeeRepository userEmployeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserClientService userClientService;
    private final UserEmployeeService userEmployeeService;

    // ✅ تسجيل دخول العميل
    @PostMapping("/login/client")
    public ResponseEntity<?> loginClient(@RequestBody LoginRequest request) {
        var client = userClientRepository.findByEmail(request.getEmail());
        if (client.isPresent() && passwordEncoder.matches(request.getPassword(), client.get().getPasswordHash())) {
            String token = jwtUtil.generateToken(buildUserDetails(client.get(), "ROLE_CLIENT"));
            return ResponseEntity.ok(new AuthResponse(token, "CLIENT"));
        }
        return ResponseEntity.status(401).body("Client login failed");
    }

    // ✅ تسجيل دخول الموظف
    @PostMapping("/login/employee")
    public ResponseEntity<?> loginEmployee(@RequestBody LoginRequest request) {
        var employee = userEmployeeRepository.findByEmail(request.getEmail());
        if (employee.isPresent() && passwordEncoder.matches(request.getPassword(), employee.get().getPasswordHash())) {
            String token = jwtUtil.generateToken(buildUserDetails(employee.get(), "ROLE_EMPLOYEE"));
            return ResponseEntity.ok(new AuthResponse(token, "EMPLOYEE"));
        }
        return ResponseEntity.status(401).body("Employee login failed");
    }

    // ✅ تسجيل دخول الأدمن
    @PostMapping("/login/admin")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginRequest request) {
        var admin = userAdminRepository.findByEmail(request.getEmail());
        if (admin.isPresent() && passwordEncoder.matches(request.getPassword(), admin.get().getPasswordHash())) {
            String token = jwtUtil.generateToken(buildUserDetails(admin.get(), "ROLE_ADMIN"));
            return ResponseEntity.ok(new AuthResponse(token, "ADMIN"));
        }
        return ResponseEntity.status(401).body("ADMIN login failed");
    }

    // ✅ تسجيل عميل + JWT
    @PostMapping("/register/client")
    public ResponseEntity<?> registerClient(@RequestBody ClientRegisterRequest request) {
        if (userClientRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists.");
        }

        UserClient client = userClientService.registerClient(
                request.getFullName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getMobileNumber(),
                request.getCompanyName(),
                request.getAddress()
        );

        String token = jwtUtil.generateToken(buildUserDetails(client, "ROLE_CLIENT"));
        return ResponseEntity.ok(new AuthResponse(token, "Client registration has been successful."));


    }

    // ✅ تسجيل موظف + JWT
    @PostMapping("/register/employee")
    public ResponseEntity<?> registerEmployee(@RequestBody EmployeeRegisterRequest request) {
        if (userEmployeeRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists.");
        }

        UserEmployee employee = userEmployeeService.registerEmployee(
                request.getFullName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getMobileNumber(),
                request.getDepartment(),
                request.getJobTitle()
        );

        String token = jwtUtil.generateToken(buildUserDetails(employee, "ROLE_EMPLOYEE"));
        return ResponseEntity.ok(new AuthResponse(token, "EMPLOYEE"));
    }

    // 🧠 دعم دالة موحدة لتوليد UserDetails حسب النوع
    private User buildUserDetails(Object user, String role) {
        String email;
        String password;

        if (user instanceof UserClient client) {
            email = client.getEmail();
            password = client.getPasswordHash();
        } else if (user instanceof UserEmployee employee) {
            email = employee.getEmail();
            password = employee.getPasswordHash();
        } else if (user instanceof UserAdmin admin) {
            email = admin.getEmail();
            password = admin.getPasswordHash();
        } else {
            throw new IllegalArgumentException("Unsupported user type");
        }

        return new User(email, password,
                java.util.List.of(() -> role));
    }
}
