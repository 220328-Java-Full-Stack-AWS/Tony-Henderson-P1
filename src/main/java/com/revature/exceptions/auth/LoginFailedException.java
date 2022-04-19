package com.revature.exceptions.auth;

public class LoginFailedException extends Exception{

    public LoginFailedException() {
        super("Login Failed. Username or password does exist/match");
    }

    public LoginFailedException(String message) {
        super(message);
    }

    public LoginFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginFailedException(Throwable cause) {
        super(cause);
    }
}
