package dev.lin.helpdesk_software_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "No hemos encontrado el ticket que querías :(")
public class TicketNotFoundException extends TicketException {

    public TicketNotFoundException(String message) {
        super(message);
    }
}