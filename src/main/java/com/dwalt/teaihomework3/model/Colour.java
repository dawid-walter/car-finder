package com.dwalt.teaihomework3.model;

public enum Colour {
    RED("Red"),
    WHITE("White"),
    BLUE("Blue"),
    GREEN("Green"),
    YELLOW("Yellow");

    private final String colourName;

    Colour(String colourName) {
        this.colourName = colourName;
    }

    public String getColourName() {
        return colourName;
    }
}
