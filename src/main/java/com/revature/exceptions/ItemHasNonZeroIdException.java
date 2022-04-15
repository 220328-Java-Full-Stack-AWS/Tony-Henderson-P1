package com.revature.exceptions;

public class ItemHasNonZeroIdException extends Exception{

    public ItemHasNonZeroIdException() {
        super();
    }

    public ItemHasNonZeroIdException(String message) {
        super(message);
    }

    public ItemHasNonZeroIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemHasNonZeroIdException(Throwable cause) {
        super(cause);
    }
}
