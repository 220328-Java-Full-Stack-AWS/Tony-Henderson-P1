package com.revature.exceptions;

public class DeleteUserUnsuccessfulException extends Exception{

    public DeleteUserUnsuccessfulException() {
        super("Deleting user was unsuccessful");
    }

    public DeleteUserUnsuccessfulException(String message) {
        super(message);
    }

    public DeleteUserUnsuccessfulException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeleteUserUnsuccessfulException(Throwable cause) {
        super(cause);
    }
}
