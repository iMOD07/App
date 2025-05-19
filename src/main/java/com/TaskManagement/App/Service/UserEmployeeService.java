package com.TaskManagement.App.Service;


import com.TaskManagement.App.Model.Role;
import com.TaskManagement.App.Model.UserEmployee;
import com.TaskManagement.App.Repository.UserEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class UserEmployeeService {

    private final UserEmployeeRepository userEmployeeRepository;

    public UserEmployee registerEmployee(String fullName,
                                         String email,
                                         String passwordHash,
                                         String mobileNumber,
                                         String department,
                                         String jobTitle) {

        // Check if email exists?
        if (userEmployeeRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"This Email is already registered");
        }

        // Check if mobile Number ?
        if (userEmployeeRepository.existsByMobileNumber(mobileNumber)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This mobile number is already registered");
        }

        // now Register User Employee
        UserEmployee userEmployee = new UserEmployee();
        userEmployee.setFullName(fullName);
        userEmployee.setEmail(email);
        userEmployee.setPasswordHash(passwordHash);
        userEmployee.setMobileNumber(mobileNumber);
        userEmployee.setDepartment(department);
        userEmployee.setJobTitle(jobTitle);
        return userEmployeeRepository.save(userEmployee);
    }

    public Optional<UserEmployee> findById(Long id) {
        return userEmployeeRepository.findById(id);
    }

    public boolean existsById(Long employeeId) {
        return userEmployeeRepository.existsById(employeeId);
    }

    // Employee search function by email
    public Optional<UserEmployee> findByEmail(String email) {
        return userEmployeeRepository.findByEmail(email);
    }

    // Get all employees
    public List<UserEmployee> getAllEmployees() {
        return userEmployeeRepository.findAll();
    }

    // Get employee by ID
    public Optional<UserEmployee> getEmployeeById(Long id) {
        return userEmployeeRepository.findById(id);
    }

    // Update Role
    @Transactional
    public UserEmployee updateEmployeeRole(Long employeeId, Role newRole) {
        UserEmployee employee = userEmployeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"This User Employee Not Found"));

        // now Set new Role
        employee.setRole(newRole);
        return userEmployeeRepository.save(employee);
    }

    // Delete Employee
    @Transactional
    public void deleteEmployee(Long employeeId) {
        // Check if User Employee is present?
        UserEmployee employee = userEmployeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"This User Employee Not Found"));

        // now Delete User
        userEmployeeRepository.delete(employee);
    }
}
