package com.revature.exceptions.user;

public class NoUserExistsException extends Exception{

    public NoUserExistsException() {
        super("No user exists with specified criteria");
    }

    public NoUserExistsException(String message) {
        super(message);
    }

    public NoUserExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoUserExistsException(Throwable cause) {
        super(cause);
    }
}
