package com.TaskManagement.App.Repository;

import com.TaskManagement.App.Model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByEmail(String email);
    Optional<Company> findByMobileNumber(String mobileNumber);
    boolean existsByEmail(String email);
    boolean existsByCompanyName(String companyName);
    boolean existsByMobileNumber(String mobileNumber);

}
