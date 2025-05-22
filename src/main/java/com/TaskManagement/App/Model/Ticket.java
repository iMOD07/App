package com.TaskManagement.App.Model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Ticket")
public class Ticket {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String title;


    @Column(nullable = false)
    private String description;


    @CreatedDate
    @JoinColumn(name = "created_at", updatable = false)
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();


    @ManyToOne(optional = false)
    @JoinColumn(name = "assigned_to", nullable = false)
    private UserAdmin assignedToAdmin;


    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id")
    private UserClient client;


    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_status")
    private TicketStatus ticketStatus = TicketStatus.IN_CREATION;


}
