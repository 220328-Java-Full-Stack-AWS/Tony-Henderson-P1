package com.revature.exceptions;

public class CreationUnsuccessfulException extends Exception{

    public CreationUnsuccessfulException() {
        super("Failed to create item");
    }

    public CreationUnsuccessfulException(String message) {
        super(message);
    }

    public CreationUnsuccessfulException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreationUnsuccessfulException(Throwable cause) {
        super(cause);
    }
}
