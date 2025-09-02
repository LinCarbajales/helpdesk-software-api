package dev.lin.helpdesk_software_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "No hemos encontrado el tema que buscas.")
public class SubjectNotFoundException extends SubjectException {

    public SubjectNotFoundException(String message) {
        super(message);
    }
}