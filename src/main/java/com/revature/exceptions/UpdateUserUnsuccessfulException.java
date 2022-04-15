package com.revature.exceptions;

public class UpdateUserUnsuccessfulException extends Exception{

    public UpdateUserUnsuccessfulException() {
        super("Failed to update user");
    }

    public UpdateUserUnsuccessfulException(String message) {
        super(message);
    }

    public UpdateUserUnsuccessfulException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpdateUserUnsuccessfulException(Throwable cause) {
        super(cause);
    }
}
