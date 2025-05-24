package com.TaskManagement.App.Controller;

import com.TaskManagement.App.Dto.CompanyRegisterRequest;
import com.TaskManagement.App.Model.Company;
import com.TaskManagement.App.Service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/company")
public class CompanyController {

    private final CompanyService companyService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<Company> registerCompany(@Valid @RequestBody CompanyRegisterRequest companyRequest,
                                                   Authentication authentication) {
        Company company = companyService.registerCompany(
                companyRequest.getEmail(),
                companyRequest.getCompanyName(),
                companyRequest.getMobileNumber(),
                companyRequest.getResponsiblePerson(),
                companyRequest.getAddress(),
                companyRequest.getVat(),
                companyRequest.getContractStart(),
                companyRequest.getContractEnd()
        );
        return ResponseEntity.ok(company);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @GetMapping("/")
    public ResponseEntity<List<Company>> getAllCompany() {
        List<Company> companies = companyService.getAllCompany();
        return ResponseEntity.ok(companies);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        Optional<Company> company = companyService.getCompanyById(id);
        return company.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{companyId}")
    public ResponseEntity<String> deleteCompany(@PathVariable Long companyId){
        companyService.deleteCompany(companyId);
        return ResponseEntity.ok("The Company deleted successfully");
    };

}