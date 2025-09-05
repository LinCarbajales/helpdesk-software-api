package dev.lin.helpdesk_software_api.Employee;

import dev.lin.helpdesk_software_api.SolvedTicket.SolvedTicketEntity;
import dev.lin.helpdesk_software_api.Ticket.TicketEntity;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "employees")
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "employee_role")
    private EmployeeRole role;
    
    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketEntity> createdTickets;
    
    @OneToMany(mappedBy = "attendee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SolvedTicketEntity> solvedTickets;

    public EmployeeEntity() {}

    public EmployeeEntity(String name, EmployeeRole role) {
        this.name = name;
        this.role = role;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EmployeeRole getRole() {
        return role;
    }

    public void setRole(EmployeeRole role) {
        this.role = role;
    }
    
    public List<TicketEntity> getCreatedTickets() {
        return createdTickets;
    }

    public void setCreatedTickets(List<TicketEntity> createdTickets) {
        this.createdTickets = createdTickets;
    }

    public List<SolvedTicketEntity> getSolvedTickets() {
        return solvedTickets;
    }

    public void setSolvedTickets(List<SolvedTicketEntity> solvedTickets) {
        this.solvedTickets = solvedTickets;
    }
}