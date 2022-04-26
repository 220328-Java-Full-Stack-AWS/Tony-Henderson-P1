package com.revature.exceptions.sql;

public class CannotDeleteForeignKeyViolationException extends Exception{

    public CannotDeleteForeignKeyViolationException() {
        super("Cannot delete, a row in another table depends on this item");
    }

    public CannotDeleteForeignKeyViolationException(String message) {
        super(message);
    }

    public CannotDeleteForeignKeyViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotDeleteForeignKeyViolationException(Throwable cause) {
        super(cause);
    }
}
