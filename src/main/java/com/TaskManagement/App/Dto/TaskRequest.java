package com.TaskManagement.App.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequest {

    @Column(nullable = false)
    @JsonProperty("title")
    @Pattern(
            regexp = "^[\\p{L} ]+$",
            message = "Full name must not contain numbers or symbols") // Arabic and English names without numbers or symbols.
    private String title;

    @Column(nullable = false)
    @JsonProperty("description")
    @Pattern(
            regexp = "^[\\p{L} ]+$",
            message = "Full name must not contain numbers or symbols") // Arabic and English names without numbers or symbols.
    private String description;


    private String status;


    private Long assignedToId;


    private LocalDateTime dueDate;


    private Long connect_to;

}