package com.lomasity.jscore.model;

public enum TeamType {
    
    HOME, AWAY, NEITHER;

    static TeamType getWithValue(String value) {
        switch(value.toUpperCase()) {
            case "HOME":
            case "H":
                return HOME;
            case "AWAY":
            case "A":
                return AWAY;
            case "NEITHER":
            case "N":
                return NEITHER;
            default:
                throw new IllegalArgumentException(value + " is not a team type");
        }
    }

}
