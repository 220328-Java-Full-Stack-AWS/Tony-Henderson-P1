package com.revature.exceptions.user;

public class EmailNotUniqueException extends Exception {

    public EmailNotUniqueException() {
        super("Email is not unique");
    }

    public EmailNotUniqueException(String message) {
        super(message);
    }

    public EmailNotUniqueException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailNotUniqueException(Throwable cause) {
        super(cause);
    }
}
