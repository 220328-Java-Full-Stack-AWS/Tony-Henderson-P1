package com.revature.exceptions.sql;

public class NotNullConstraintException extends Exception{

    public NotNullConstraintException() {
    }

    public NotNullConstraintException(String message) {
        super(message);
    }

    public NotNullConstraintException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotNullConstraintException(Throwable cause) {
        super(cause);
    }
}
