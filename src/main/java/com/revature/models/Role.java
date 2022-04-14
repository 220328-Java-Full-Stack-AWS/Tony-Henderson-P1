package com.revature.models;

/**
 * Users within the ERS application are categorized within the following roles:
 * <ul>
 *     <li>Employee</li>
 *     <li>Finance Manager</li>
 * </ul>
 *
 * Employees are the standard role for Users within the application.
 *
 * Finance Managers have additional permissions to process reimbursement requests.
 * <ul>
 *     <li>Finance Managers can submit reimbursement requests</li>
 *     <li>Finance Managers cannot process their own requests</li>
 * </ul>
 *
 * @author Center of Excellence
 */
public enum Role {

    EMPLOYEE("EMPLOYEE"),
    FINANCE_MANAGER("FINANCE_MANAGER");

    private String name;

    Role(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String toTitleCase(){
        StringBuilder sb = new StringBuilder();
        boolean convertNext = true;
        for(char c : this.name.toCharArray()){
            if(c == '_'){
                convertNext = true;
                sb.append(' ');
                continue;
            }
            if(convertNext){
                convertNext = false;
                sb.append(Character.toTitleCase(c));
            }
            else {
                sb.append(Character.toLowerCase(c));
            }
        }
        return sb.toString();
    }
}
