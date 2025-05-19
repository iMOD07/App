package com.TaskManagement.App.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email; //

    @Column(nullable = false)
    private String companyName; //

    @Column(nullable = false, unique = true, length = 14)
    private String mobileNumber; //

    @Column(nullable = false)
    private String address; //

    @Column(nullable = false)
    private String vat;

    @Column(nullable = false)
    private LocalDate contractStart;

    @Column(nullable = false)
    private LocalDate contractEnd;

}
