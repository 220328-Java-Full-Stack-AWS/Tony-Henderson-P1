package com.revature.exceptions;

public class UpdateUnsuccessfulException extends Exception{

    public UpdateUnsuccessfulException() {
        super("Failed to update item");
    }

    public UpdateUnsuccessfulException(String message) {
        super(message);
    }

    public UpdateUnsuccessfulException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpdateUnsuccessfulException(Throwable cause) {
        super(cause);
    }
}
