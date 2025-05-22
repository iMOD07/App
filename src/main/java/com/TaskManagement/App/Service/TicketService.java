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
        ticket.setCreatedBy(userClient);
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


    // Get Ticket By User (ADMIN User)
    public List<Ticket> getTicketsByUserId(Long userId) {
        UserClient userClient = userClientRepository.findById(userId)
                .orElseThrow(()-> new ApiException("لم يتم العثور على العميل","Client not found"));

        List<Ticket> tickets = ticketRepository.findByClientId(userId);
        if (tickets.isEmpty()) {
            throw new ApiException("لم يتم العثور على تذاكر لهذا المستخدم","No tickets found for this user");
        }
        return tickets;
    }


    // For client only (to view their tickets)
    public List<Ticket> getTicketsForCurrentClient(UserClient currentClient){
        List<Ticket> tickets = ticketRepository.findByClientId(currentClient.getId());
        if (tickets.isEmpty()) {
            throw new ApiException("ليس لديك تذاكر","You have no tickets");
        }
        return tickets;
    }


    // Client can delete only their own ticket
    public void deleteTicketByClient(UserClient client) {
        List<Ticket> tickets = ticketRepository.findByClientId(client.getId());

        if (tickets.isEmpty()) {
            throw new ApiException("ليس لديك تذاكر", "You have no tickets");
        }
        ticketRepository.delete(tickets.get(0));
    }


    // Admin can delete any ticket for a specific user
    public void deleteTicketByAdmin(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ApiException(" لا يوجد تذكرة بهذا الرقم","There is no ticket for this number."));
        ticketRepository.delete(ticket);
    }


}
