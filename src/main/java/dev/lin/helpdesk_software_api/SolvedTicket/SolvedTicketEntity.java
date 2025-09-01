package dev.lin.helpdesk_software_api.SolvedTicket;

import dev.lin.helpdesk_software_api.Ticket.TicketEntity;
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

    @Column(name = "attendee_id")
    private Long attendeeId; // FK

    @Column(name = "solved_at")
    private LocalDateTime solvedAt;

    public SolvedTicketEntity() {}

    public SolvedTicketEntity(TicketEntity ticket, Long attendeeId) {
        this.ticket = ticket;
        this.attendeeId = attendeeId;
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

    public Long getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(Long attendeeId) {
        this.attendeeId = attendeeId;
    }

    public LocalDateTime getSolvedAt() {
        return solvedAt;
    }

    public void setSolvedAt(LocalDateTime solvedAt) {
        this.solvedAt = solvedAt;
    }
}