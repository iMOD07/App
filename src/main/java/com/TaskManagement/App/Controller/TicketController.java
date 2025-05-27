package com.TaskManagement.App.Controller;

import com.TaskManagement.App.Dto.TicketRequest;
import com.TaskManagement.App.Model.*;
import com.TaskManagement.App.Repository.UserClientRepository;
import com.TaskManagement.App.Security.SecurityUtils;
import com.TaskManagement.App.Service.TicketService;
import jakarta.validation.Valid;
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
@CrossOrigin
@RequestMapping("/api/tickets")
public class TicketController {


    private final TicketService ticketService;
    private final UserClientRepository userClientRepository;
    private final SecurityUtils securityUtils;



    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/create")
    public ResponseEntity<Ticket> createTicket(@Valid @RequestBody TicketRequest request,
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

    // For ADMIN : View any Client tickets by ID
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Ticket>> getTicketsByUser (@PathVariable Long userId) {
        List<Ticket> tickets = ticketService.getTicketsByUserId(userId);
        return ResponseEntity.ok(tickets);
    }

    // For the Client : He only sees his tickets
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/my-tickets")
    public ResponseEntity<List<Ticket>> getClientTickets(Authentication authentication) {
        UserClient current = securityUtils.getCurrentClient(authentication);
        List<Ticket> tickets = ticketService.getTicketsForCurrentClient(current);
        return ResponseEntity.ok(tickets);
    }


    // Delete ticket by customer (only his ticket)
    @PreAuthorize("hasRole('CLIENT')")
    @DeleteMapping("/client/deleteMyTicket")
    public ResponseEntity<String> deleteMyTicket(Authentication authentication) {

        UserClient current = securityUtils.getCurrentClient(authentication);

        ticketService.deleteTicketByClient(current);

        return ResponseEntity.ok("Ticket deleted successfully");
    }

    // Delete ticket by Just ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{ticketId}")
    public ResponseEntity<String> deleteTicketByAdmin(@PathVariable Long ticketId) {
        ticketService.deleteTicketByAdmin(ticketId);
        return ResponseEntity.ok("Ticket deleted successfully");
    }

}
