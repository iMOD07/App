package com.TaskManagement.App.Controller;


import com.TaskManagement.App.Dto.TicketRequest;
import com.TaskManagement.App.Model.*;
import com.TaskManagement.App.Repository.UserClientRepository;
import com.TaskManagement.App.Security.SecurityUtils;
import com.TaskManagement.App.Service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tickets")
public class TicketController {


    private final TicketService ticketService;
    private final UserClientRepository userClientRepository;
    private final SecurityUtils securityUtils;



    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/create")
    public ResponseEntity<Ticket> createTicket(@RequestBody TicketRequest request,
                                               Authentication authentication) {

        String email = authentication.getName();
        Ticket ticket = ticketService.createTicket(
                request.getTitle(),
                request.getDescription(),
                email
        );
        return ResponseEntity.ok(ticket);
    }

    // Update Ticket close and open
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/status/{ticketId}")
    public ResponseEntity<Ticket> updateTicketStatus(@PathVariable Long ticketId,
                                                     @RequestParam TicketStatus status) {
        Ticket updated = ticketService.updateTicketStatus(ticketId, status);
        return ResponseEntity.ok(updated);
    }


    // Just ADMIN he Get All Ticket
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<Ticket>> getAllTicket() {
        List<Ticket> all = ticketService.getAllTicket();
        System.out.println("admin Admin fetched all tickets, count = " + all.size());
        return ResponseEntity.ok(all);
    }


    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Ticket>> getUserTickets(@PathVariable("userId") Long userId,
                                                       Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        UserClient current = null;
        if (!isAdmin) {
            current = securityUtils.getCurrentClient(authentication);
        }

        List<Ticket> tickets = ticketService.getTicketsByUser(userId, current, isAdmin);

        return ResponseEntity.ok(tickets);
    }



    // Delete ticket
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    @DeleteMapping("/{ticketId}")
    public ResponseEntity<String> deleteTicket(@PathVariable Long ticketId,
                                               Authentication authentication) {
        UserClient currentUser = (UserClient) authentication.getPrincipal();

        if (currentUser.getRole() == Role.CLIENT){
            // Client: Delete only his tickets
            ticketService.deleteTicket(ticketId, currentUser.getId());
        } else if (currentUser.getRole().name().equals("ADMIN")) {
            // Admin: Delete any ticket without needing user ID
            ticketService.deleteTicket(ticketId);
        }
        return ResponseEntity.ok("Ticket deleted successfully");
    }









}
