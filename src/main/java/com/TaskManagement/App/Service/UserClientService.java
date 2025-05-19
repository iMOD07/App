package com.TaskManagement.App.Service;

import com.TaskManagement.App.Model.UserClient;
import com.TaskManagement.App.Repository.UserClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserClientService {

    private final UserClientRepository userClientRepository;

    public UserClient registerClient(String fullName,
                                     String email,
                                     String passwordHash,
                                     String mobileNumber,
                                     String companyName,
                                     String address) {

        // check if email exists ?
        if (userClientRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"This Email is already registered");
        }

        // Check if mobile Number ?
        if (userClientRepository.existsByMobileNumber(mobileNumber)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This mobile number is already registered");
        }

        // now Register User Client
        UserClient userClient = new UserClient();
        userClient.setFullName(fullName);
        userClient.setEmail(email);
        userClient.setPasswordHash(passwordHash);
        userClient.setMobileNumber(mobileNumber);
        userClient.setCompanyName(companyName);
        userClient.setAddress(address);
        return userClientRepository.save(userClient);
    }

    public Optional<UserClient> findById(Long id) {
        return userClientRepository.findById(id);
    }

    public boolean existsById(Long clientId) {
        return userClientRepository.existsById(clientId);
    }

    // Client search function by email
    public Optional<UserClient> findByEmail(String email) {
        return userClientRepository.findByEmail(email);
    }

    // Get all Client
    public List<UserClient> getAllClients() {
        return userClientRepository.findAll();
    }

    // Get a client by ID
    public Optional<UserClient> getClientById(Long id) {
        return userClientRepository.findById(id);
    }

    // Delete Client
    @Transactional
    public void deleteClient(Long clientId) {
        // check if user client is present?
        UserClient client = userClientRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"This User Client Not Found"));

        // now Delete User
        userClientRepository.delete(client);
    }

}
