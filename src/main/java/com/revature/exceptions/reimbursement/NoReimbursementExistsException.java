package com.revature.exceptions.reimbursement;

public class NoReimbursementExistsException extends Exception{

    public NoReimbursementExistsException() {
        super("No Reimbursement exists with specified criteria");
    }

    public NoReimbursementExistsException(String message) {
        super(message);
    }

    public NoReimbursementExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoReimbursementExistsException(Throwable cause) {
        super(cause);
    }
}
