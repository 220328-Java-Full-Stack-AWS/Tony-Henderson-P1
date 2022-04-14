package com.revature.models;

import com.revature.utils.Utils;

public enum ReimbursementType implements Formatable {

    LODGING("LODGING"),
    FOOD("FOOD"),
    TRAVEL("TRAVEL"),
    SUPPLIES("SUPPLIES");

    private String name;

    ReimbursementType(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return this.name;
    }

    @Override
    public String toTitleCase(){
        return Utils.stringToTitleCase(this.name);
    }
}
