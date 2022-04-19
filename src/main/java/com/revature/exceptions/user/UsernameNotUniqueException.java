package com.revature.exceptions.user;

public class UsernameNotUniqueException extends Exception {

    public UsernameNotUniqueException() {
        super();
    }

    public UsernameNotUniqueException(String message) {
        super(message);
    }

    public UsernameNotUniqueException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameNotUniqueException(Throwable cause) {
        super(cause);
    }

    public UsernameNotUniqueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
