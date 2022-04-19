package com.revature.exceptions.auth;

public class NotAuthorizedException extends Exception{

    public NotAuthorizedException() {
        super("User not authorized to make this action");
    }

    public NotAuthorizedException(String message) {
        super(message);
    }

    public NotAuthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAuthorizedException(Throwable cause) {
        super(cause);
    }
}
