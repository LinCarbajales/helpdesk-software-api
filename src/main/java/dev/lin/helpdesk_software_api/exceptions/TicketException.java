package dev.lin.helpdesk_software_api.exceptions;

public class TicketException extends RuntimeException {

    public TicketException(String message) {
        super(message);
    }

    public TicketException(String message, Throwable cause) {
        super(message, cause);
    }
}