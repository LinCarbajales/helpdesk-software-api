package dev.lin.helpdesk_software_api.Ticket;

public enum TicketStatus {
    OPEN("Open", "Ticket created and pending attention"),
    ATTENDED("Attended", "Ticket has been resolved and attended to");
    
    private final String displayName;
    private final String description;
    
    TicketStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    // Método para validar transiciones
    public boolean canTransitionTo(TicketStatus newStatus) {
        return switch (this) {
            case OPEN -> newStatus == ATTENDED;
            case ATTENDED -> false;
        };
    }
}