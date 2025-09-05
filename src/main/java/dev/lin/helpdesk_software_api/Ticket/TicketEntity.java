package dev.lin.helpdesk_software_api.Ticket;

import java.time.LocalDateTime;

import dev.lin.helpdesk_software_api.Employee.EmployeeEntity;
import dev.lin.helpdesk_software_api.SolvedTicket.SolvedTicketEntity;
import dev.lin.helpdesk_software_api.Subject.SubjectEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "tickets")
public class TicketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private EmployeeEntity requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "subject_id",
        referencedColumnName = "id_subject",
        nullable = false
    )
    private SubjectEntity subject;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TicketStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private SolvedTicketEntity solvedTicket;

    public TicketEntity() {
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = TicketStatus.OPEN;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public TicketEntity(EmployeeEntity requester, SubjectEntity subject, String description) {
        this.requester = requester;
        this.subject = subject;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public EmployeeEntity getRequester() {
        return requester;
    }

    public SubjectEntity getSubject() {
        return subject;
    }

    public String getDescription() {
        return description;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public SolvedTicketEntity getSolvedTicket() {
        return solvedTicket;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setRequester(EmployeeEntity requester) {
        this.requester = requester;
    }

    public void setSubject(SubjectEntity subject) {
        this.subject = subject;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setSolvedTicket(SolvedTicketEntity solvedTicket) {
        this.solvedTicket = solvedTicket;
    }
    
}
