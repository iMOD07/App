package com.TaskManagement.App.Service;


import com.TaskManagement.App.Exception.ApiException;
import com.TaskManagement.App.Model.*;
import com.TaskManagement.App.Repository.TicketRepository;
import com.TaskManagement.App.Repository.UserAdminRepository;
import com.TaskManagement.App.Repository.UserClientRepository;
import com.TaskManagement.App.Repository.UserEmployeeRepository;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserAdminRepository userAdminRepository;
    private final UserEmployeeRepository userEmployeeRepository;
    private final UserClientRepository userClientRepository;
    private final UserClientService userClientService;


    public Ticket createTicket(String title,
                               String description,
                               String clientEmail){

        UserAdmin admin = userAdminRepository.findByRole(Role.ADMIN)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        // 1 Extract the Client from the email
        UserClient userClient = userClientRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        // Make Sure Client don't have any ticket
        List<TicketStatus> activeStatuses = List.of(
                TicketStatus.IN_CREATION,
                TicketStatus.IN_PROGRESS,
                TicketStatus.OPEN
        );
        long count = ticketRepository.countByClientAndTicketStatusIn(userClient, activeStatuses);
        if (count> 0) {
            throw new ApiException("لديك تذكرة مفتوحة بالفعل", "You already have an active ticket");
        }

        Ticket ticket = new Ticket();
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setClient(userClient);
        ticket.setAssignedToAdmin(admin);
        ticket.setTicketStatus(TicketStatus.IN_CREATION);
        ticket.setCreatedAt(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }


    // Update Ticket
    public Ticket updateTicketStatus(Long ticketId, TicketStatus newStatus) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        return ticketRepository.save(ticket);
    }


    // Just Admin He Get All Ticket
    public List<Ticket> getAllTicket() {
        List<Ticket> all = ticketRepository.findAll();
        if (all.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Tickets Found");
        }
        return all;
    }


    // Get Ticket By User
    public List<Ticket> getTicketsByUser(Long userId, UserClient currentClient, boolean isAdmin) {

        // 1 You must check with the client first
        UserClient userclient = userClientRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // 2 If you are not an ADMIN and you want to view tickets other than your ticket => forbidden
        if (!isAdmin && !currentClient.getId().equals(userclient.getId())) {
            throw new AccessDeniedException("You can only view your own tickets");
        }

        // 3 Get Ticket
        List<Ticket> tickets = ticketRepository.findByClientId(userId);
        if (tickets.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No tickets for this user");
        }
        return tickets;
    }




    // Delete Ticket
    public void deleteTicket(Long ticketId, Long clientId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        // check that the ticket holder is the authenticated customer
        if (!ticket.getClient().getId().equals(clientId)) {
            throw new AccessDeniedException("You can only delete your own ticket");
        }
        ticketRepository.delete(ticket);
    }

    // Deleted by admin: No ownership verification
    public void deleteTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket Not Found"));
        ticketRepository.delete(ticket);
    }

}
