package com.TaskManagement.App.Dto;

import com.TaskManagement.App.validation.ValidCommonEmail;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ClientRegisterRequest {

    @Column(nullable = false)
    @JsonProperty("fullName")
    @Pattern(
            regexp = "^[\\p{IsArabic}a-zA-Z]{3,}$",
            message = "Full name must contain only Arabic or English letters and be at least 3 characters long")
    private String fullName;


    @Column(nullable = false)
    @JsonProperty("email")
    @NotBlank(message = "Email is required") // Invalid input error message
    @ValidCommonEmail(message = "You must email be from a well-known provider such as Gmail or Outlook")
    private String email;


    @Column(nullable = false)
    @JsonProperty("password")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
            message = "Password must contain:\n" + "At least one uppercase letter\n" + "At least one lowercase letter\n" + "At least one number\n" + "At least one special character (e.g., @, #, !, etc.)"
    )
    private String password;


    @Column(nullable = false)
    @JsonProperty("mobileNumber")
    @Pattern(
            regexp = "^05[0-9]{8}$",
            message = "The Mobile Number must be a valid Saudi number, consisting of 10 digits")
    private String mobileNumber;



    @Column(nullable = false)
    @JsonProperty("companyName")
    @Pattern(
            regexp = "^[\\p{L} ]+$",
            message = "Must contain only Arabic or English letters")
    private String companyName;


    @Column(nullable = false)
    @JsonProperty("address")
    @Pattern(
            regexp = "^[\\p{L} ]+$",
            message = "Must contain only Arabic or English letters")
    private String address;

}
