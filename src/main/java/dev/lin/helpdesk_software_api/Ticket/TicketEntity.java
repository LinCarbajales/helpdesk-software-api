package dev.lin.helpdesk_software_api.Ticket;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Entity
@Table(name = "tickets")

public class TicketEntity {

    private Long id;
    private String description;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;
    
    // Foreign Keys
    private Long requesterId;    // Relación con users (solicitante)
    private Long subjectId;      // Relación con subjects


    public TicketEntity() {
    }

    public TicketEntity(Long id, Long requesterId, Long subjectId, String description,
                       String status) {
        this.requesterId = requesterId;
        this.subjectId = subjectId;
        this.description = description;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.resolvedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
}
