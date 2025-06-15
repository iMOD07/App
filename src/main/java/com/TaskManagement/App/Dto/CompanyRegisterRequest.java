package com.TaskManagement.App.Dto;

import com.TaskManagement.App.validation.ValidCommonEmail;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyRegisterRequest {

    @Column(nullable = false)
    @JsonProperty("companyName")
    @Pattern(
            regexp = "^[\\p{L} ]+$",
            message = "Company Name must not contain numbers or symbols") // Arabic and English names without numbers or symbols.
    private String companyName;


    @Column(nullable = false)
    @JsonProperty("email")
    @NotBlank(message = "Email is required") // Invalid input error message
    @ValidCommonEmail(message = "You must email be from a well-known provider such as Gmail or Outlook")
    private String email;


    @Column(nullable = false)
    @JsonProperty("mobileNumber")
    @Pattern(
            regexp = "^05[0-9]{8}$",
            message = "Mobile number must be a valid Saudi number starting with 05")
    private String mobileNumber;

    @Column(nullable = false)
    @JsonProperty("responsiblePerson")
    @Pattern(
            regexp = "^[\\p{L} ]+$",
            message = "Must contain only Arabic or English letters")
    private String responsiblePerson;


    @Column(nullable = false)
    @JsonProperty("address")
    @Pattern(
            regexp = "^[\\p{L} ]+$",
            message = "Must contain only Arabic or English letters")
    private String address;


    @Column(nullable = false)
    @JsonProperty("vat")
    @Pattern(
            regexp = "^\\d{15}$",
            message = "Must contain exactly 15 digits")
    private String vat;


    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("contractStart")
    private LocalDate contractStart;


    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("contractEnd")
    private LocalDate contractEnd;

}


