package com.revature.models;

import com.revature.utils.Utils;

/**
 * Reimbursements within the ERS application transition through the following statuses:
 * <ul>
 *     <li>Pending</li>
 *     <li>Approved</li>
 *     <li>Denied</li>
 * </ul>
 *
 * Once reimbursements are processed, their final status cannot be changed.
 * A new reimbursement must be submitted.
 *
 * @author Center of Excellence
 */
public enum Status implements Formatable {

    PENDING("PENDING"),
    APPROVED("APPROVED"),
    DENIED("DENIED");

    private String name;

    Status(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String toTitleCase() {
        return Utils.stringToTitleCase(this.name);
    }
}
