package com.revature.exceptions.auth;

public class CannotParseJWT extends Exception{

    public CannotParseJWT() {
        super("Cannot parse the JWT");
    }

    public CannotParseJWT(String message) {
        super(message);
    }

    public CannotParseJWT(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotParseJWT(Throwable cause) {
        super(cause);
    }
}
