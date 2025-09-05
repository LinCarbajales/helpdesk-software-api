package dev.lin.helpdesk_software_api.exceptions;

public class SubjectException extends RuntimeException {

    public SubjectException(String message) {
        super(message);
    }

    public SubjectException(String message, Throwable cause) {
        super(message, cause);
    }
}