package com.TaskManagement.App.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_client")
public class UserClient {

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
    private String companyName;

    @Column(nullable = false)
    private String address;

    @CreatedDate
    @JoinColumn(name = " date_created", updatable = false)
    @Column(nullable = false)
    private LocalDateTime dateCreated = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.CLIENT;
    // Activate always for permanent = client Role
    @PrePersist
    protected void onCreate() {
        if (role == null) {
            role = Role.CLIENT;
        }
    }
}
