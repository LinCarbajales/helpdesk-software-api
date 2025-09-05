package dev.lin.helpdesk_software_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "No hemos encontrado este ticket como resuelto.")
public class SolvedTicketNotFoundException extends SolvedTicketException {

    public SolvedTicketNotFoundException(String message) {
        super(message);
    }
}