package com.revature.exceptions;

public class DeleteUnsuccessfulException extends Exception{

    public DeleteUnsuccessfulException() {
        super("Deleting user was unsuccessful");
    }

    public DeleteUnsuccessfulException(String message) {
        super(message);
    }

    public DeleteUnsuccessfulException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeleteUnsuccessfulException(Throwable cause) {
        super(cause);
    }
}
