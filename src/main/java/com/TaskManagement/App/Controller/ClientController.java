package com.TaskManagement.App.Controller;

import com.TaskManagement.App.Exception.ResourceLockedException;
import com.TaskManagement.App.Exception.ResourceNotFoundException;
import com.TaskManagement.App.Model.Role;
import com.TaskManagement.App.Model.UserClient;
import com.TaskManagement.App.Model.UserEmployee;
import com.TaskManagement.App.Service.UserClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final UserClientService userClientService;

    //Get All Clients - only ADMIN
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @GetMapping("/")
    public ResponseEntity<List<UserClient>> getAllClients() {
        List<UserClient> clients = userClientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    // Get Employee by ID - only Admin
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @GetMapping("/{id}")
    public ResponseEntity<UserClient> getClientById(@PathVariable Long id) {
        Optional<UserClient> clients = userClientService.getClientById(id);
        return clients.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Delete Client by ADMIN and By EMPLOYEE Yourself
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    @DeleteMapping("/{clientId}")
    public ResponseEntity<String> deleteUserClient(@PathVariable Long clientId,
                                                   Authentication authentication) {

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserClient userClient) {
            boolean isAdminClient = userClient.getRole() == Role.ADMIN;

            // Only regular CLIENT can't delete others
            if (!isAdminClient && userClient.getId().equals(clientId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You do not have permission to delete another CLIENT.");
            }
        }

        // AdminUser or AdminClient is allowed to delete
        try {
            userClientService.deleteClient(clientId);
            return ResponseEntity.ok("The CLIENT has been successfully deleted.");
        } catch (ResourceNotFoundException notFound) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFound.getMessage());
        } catch (ResourceLockedException Conflict) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Conflict.getMessage());
        } catch (Exception serverError) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("You cannot delete the account because you have Ticket");
        }
    }

}
