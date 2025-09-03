package dev.lin.helpdesk_software_api.SolvedTicket;

import dev.lin.helpdesk_software_api.Ticket.TicketEntity;
import dev.lin.helpdesk_software_api.Employee.EmployeeEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "solved_tickets")
public class SolvedTicketEntity {

    @Id
    @Column(name = "ticket_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "ticket_id")
    private TicketEntity ticket;

    @ManyToOne
    @JoinColumn(name = "attendee_id")
    private EmployeeEntity attendee; // FK

    @Column(name = "solved_at")
    private LocalDateTime solvedAt;

    public SolvedTicketEntity() {}

    public SolvedTicketEntity(TicketEntity ticket, EmployeeEntity attendee) {
        this.ticket = ticket;
        this.attendee = attendee;
    }
    
    @PrePersist
    protected void onPersist() {
        this.solvedAt = LocalDateTime.now();
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TicketEntity getTicket() {
        return ticket;
    }

    public void setTicket(TicketEntity ticket) {
        this.ticket = ticket;
    }

    public EmployeeEntity getAttendee() {
        return attendee;
    }

    public void setAttendee(EmployeeEntity attendee) {
        this.attendee = attendee;
    }

    public LocalDateTime getSolvedAt() {
        return solvedAt;
    }

    public void setSolvedAt(LocalDateTime solvedAt) {
        this.solvedAt = solvedAt;
    }
}