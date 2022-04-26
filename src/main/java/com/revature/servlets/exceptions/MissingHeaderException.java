package com.revature.servlets.exceptions;

public class MissingHeaderException extends Exception{

    public MissingHeaderException() {
        super("You are missing a required header");
    }

    public MissingHeaderException(String message) {
        super(message);
    }

    public MissingHeaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingHeaderException(Throwable cause) {
        super(cause);
    }
}
