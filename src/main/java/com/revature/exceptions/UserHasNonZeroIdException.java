package com.revature.exceptions;

public class UserHasNonZeroIdException extends Exception{

    public UserHasNonZeroIdException() {
        super();
    }

    public UserHasNonZeroIdException(String message) {
        super(message);
    }

    public UserHasNonZeroIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserHasNonZeroIdException(Throwable cause) {
        super(cause);
    }
}
