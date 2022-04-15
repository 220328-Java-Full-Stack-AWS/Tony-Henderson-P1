package com.revature.exceptions;

public class DeleteUnsuccessfulException extends Exception{

    public DeleteUnsuccessfulException() {
        super("Failed to delete item");
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
