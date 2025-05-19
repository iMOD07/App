package com.TaskManagement.App.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_employee")
public class UserEmployee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false, unique = true, length = 14)
    private String mobileNumber;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String jobTitle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.EMPLOYEE;
    // Activate always for permanent = client Role
    @PrePersist
    protected void onCreate() {
        if (role == null) {
            role = Role.CLIENT;
        }
    }

}