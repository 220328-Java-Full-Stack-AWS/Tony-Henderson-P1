package com.revature.servlets.responses;

public enum Headers {

    REIMBURSEMENT_ID("Reimbursement-Id"),
    AUTHORIZATION("Authorization"),
    USER_ID("User-Id");

    private String name;

    Headers(String name){
        this.name = name;
    }


    @Override
    public String toString() {
        return this.name;
    }
}
