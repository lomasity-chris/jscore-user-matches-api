package com.lomasity.jscore.badminton.model;

public enum InputParams {

    POINTS_TARGET("pointsTarget", true),
    POINTS_PLAYED("pointsPlayed", false),
    POINTS_HISTORY("pointsHistory", false),
    MAX_GAMES("maxGames", true),
    SETTING("setting", true),
    FINISHED("finished", true),
    GAMES("games", true);

    private final String name;
    private final boolean required;

    InputParams(String name, boolean required) {
        this.name = name;
        this.required = required;
    }

    public String getName() {
        return this.name;
    }

    public boolean isRequired() {
        return this.required;
    }

}
