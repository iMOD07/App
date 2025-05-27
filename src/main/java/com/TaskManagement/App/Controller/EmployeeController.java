package com.TaskManagement.App.Controller;

import com.TaskManagement.App.Exception.ResourceLockedException;
import com.TaskManagement.App.Exception.ResourceNotFoundException;
import com.TaskManagement.App.Model.Role;
import com.TaskManagement.App.Model.UserEmployee;
import com.TaskManagement.App.Service.UserEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/employees")
public class EmployeeController {

    private final UserEmployeeService userEmployeeService;

    // Get All Employees - only ADMIN
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @GetMapping("/")
    public ResponseEntity<List<UserEmployee>> getAllEmployees() {
        List<UserEmployee> employees = userEmployeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    // Get Employee by ID - only Admin
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @GetMapping("/{id}")
    public ResponseEntity<UserEmployee> getEmployeeById(@PathVariable Long id) {
        Optional<UserEmployee> employee = userEmployeeService.getEmployeeById(id);
        return employee.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Update Role to SUPERVISOR
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateRole/{employeeId}")
    public ResponseEntity<?> updateEmployeeRole(@PathVariable Long employeeId,
                                                @RequestParam String role,
                                                Authentication authentication) {

        Object principal = authentication.getPrincipal();

        Role newRole;
        try {
            newRole = Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid role: " + role);
        }

        // Only allow changing role to SUPERVISOR
        if (newRole != Role.SUPERVISOR) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only SUPERVISOR role assignment is allowed.");
        }

        if (principal instanceof UserEmployee currentUser) {
            boolean isAdminEmp = currentUser.getRole() == Role.ADMIN;

            // Prevent changing the role for himself
            if (isAdminEmp && currentUser.getId().equals(employeeId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You are not allowed to change your own role.");
            }
        }
        UserEmployee updatedEmployee = userEmployeeService.updateEmployeeRole(employeeId, newRole);
        return ResponseEntity.ok(updatedEmployee);
    }



    // Delete Account by ADMIN and by EMPLOYEE Yourself
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<String> deleteUserEmployee(@PathVariable Long employeeId,
                                                     Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserEmployee userEmployee) {
            boolean isAdminEmployee = userEmployee.getRole() == Role.ADMIN;

            // Only regular EMPLOYEE can't delete others
            if (!isAdminEmployee && !userEmployee.getId().equals(employeeId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You do not have permission to delete another Employee.");
            }
        }

        // AdminUser or AdminEmployee is allowed to delete
        try {
            userEmployeeService.deleteEmployee(employeeId);
            return ResponseEntity.ok("The Employee has been successfully deleted.");
        } catch (ResourceNotFoundException notFound) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFound.getMessage());
        } catch (ResourceLockedException Conflict) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Conflict.getMessage());
        } catch (Exception ServerError) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("You cannot delete the account because you have Tasks.");
        }
    }

}
