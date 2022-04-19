package com.revature.exceptions.reimbursement;

public class ReimbursementNoLongerPendingException extends Exception{

    public ReimbursementNoLongerPendingException() {
        super("The reimbursement is no longer pending");
    }

    public ReimbursementNoLongerPendingException(String message) {
        super(message);
    }

    public ReimbursementNoLongerPendingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReimbursementNoLongerPendingException(Throwable cause) {
        super(cause);
    }
}
