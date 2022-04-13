package com.revature.models;

public enum ReimbursementTypes {

    LODGING{
        @Override
        public String toString() {
            return "Lodging";
        }
    },
    FOOD{
        @Override
        public String toString() {
            return "Food";
        }
    },
    TRAVEL{
        @Override
        public String toString() {
            return "Travel";
        }
    },
    SUPPLIES{
        @Override
        public String toString(){
            return "Supplies";
        }
    }

}
