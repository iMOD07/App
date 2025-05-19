package com.TaskManagement.App.Service;

import com.TaskManagement.App.Model.Company;
import com.TaskManagement.App.Model.UserEmployee;
import com.TaskManagement.App.Repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;


    public Company registerCompany(String email,
                                   String companyName,
                                   String mobileNumber,
                                   String address,
                                   String vat) {

        // check if company exists?
        if (companyRepository.existsByCompanyName(companyName)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"This Company is already registered");
        }

        // check if Email exists?
        if (companyRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"This Email is already registered");
        }

        // existsByMobileNumber
        if (companyRepository.existsByMobileNumber(mobileNumber)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"This Mobile Number is already registered");
        }

        // now Register Company
        Company company = new Company();
        company.setEmail(email);
        company.setCompanyName(companyName);
        company.setMobileNumber(mobileNumber);
        company.setAddress(address);
        company.setVat(vat);
        return companyRepository.save(company);
    }

    public Optional<Company> findById(Long id) {
        return companyRepository.findById(id);
    }

    public boolean existsById(Long companyId) {
        return companyRepository.existsById(companyId);
    }


    // Get All Company
    public List<Company> getAllCompany() {
        return companyRepository.findAll();
    }

    // Get Company by ID
    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }

    // Company search function by email
    public Optional<Company> findByEmail(String email) {
        return companyRepository.findByEmail(email);
    }


    // Delete Company
    @Transactional
    public void deleteCompany(Long companyId) {
        //check if company is present?
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"the company not found .."));

        // now Delete Company
        companyRepository.delete(company);
    }



}
