package com.revature.exceptions.user;

public class CantParseUserException extends Exception{

    public CantParseUserException() {
        super("Cannot parse user");
    }

    public CantParseUserException(String message) {
        super(message);
    }

    public CantParseUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public CantParseUserException(Throwable cause) {
        super(cause);
    }
}
