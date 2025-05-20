package com.TaskManagement.App.Repository;

import com.TaskManagement.App.Model.Ticket;
import com.TaskManagement.App.Model.TicketStatus;
import com.TaskManagement.App.Model.UserClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByAssignedToAdmin_Id(Long id);
    List<Ticket> findByClientId(Long clientId);
    boolean existsByAssignedToAdmin_Id(Long adminId);
    boolean existsByClientAndTicketStatus(UserClient client, TicketStatus status);
    boolean existsByClient(UserClient client);

    long countByClientAndTicketStatusIn(UserClient client, List<TicketStatus> statuses);


}
