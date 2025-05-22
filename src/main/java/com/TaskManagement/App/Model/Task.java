package com.TaskManagement.App.Model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "task")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String title;


    @Column(nullable = false)
    private String description;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.IN_PROGRESS;


    @CreatedDate
    @JoinColumn(name = "created_at", updatable = false)
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();


    @Column(nullable = false)
    private LocalDateTime dueDate;


    @ManyToOne
    @JoinColumn(name = "assigned_to", nullable = false)
    private UserEmployee assignedTo;


    @OneToOne
    @JoinColumn(name = "connect_to")
    private UserClient connectTo;


}
