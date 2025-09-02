package dev.lin.helpdesk_software_api.exceptions;

public class SolvedTicketException extends RuntimeException {

    public SolvedTicketException(String message) {
        super(message);
    }

    public SolvedTicketException(String message, Throwable cause) {
        super(message, cause);
    }
}