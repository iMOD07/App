package com.TaskManagement.App.Dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {

    @Column(nullable = false)
    @NotBlank(message = "Email is required") // Invalid input error message
    @Email(message = "Email format is invalid") // You must enter a valid email address.
    private String email;

    @Column(nullable = false)
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
            message = "Password must be at least 8 characters and contain uppercase, lowercase, digit, and special character")
    private String password;
}
