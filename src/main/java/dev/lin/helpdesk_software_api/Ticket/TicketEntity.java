package dev.lin.helpdesk_software_api.Ticket;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;


public class TicketEntity {

    
    private Long id;
    private Long requesterId;    // Relación con users (solicitante)
    private Long subjectId;      // Relación con subjects
    private String description;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;
    
    // Foreign Keys



    public TicketEntity() {
    }

    public TicketEntity(Long id, Long requesterId, Long subjectId, String description,
                       String status) {
        this.id = id;
        this.requesterId = requesterId;
        this.subjectId = subjectId;
        this.description = description;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.resolvedAt = LocalDateTime.now();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }
    
}
