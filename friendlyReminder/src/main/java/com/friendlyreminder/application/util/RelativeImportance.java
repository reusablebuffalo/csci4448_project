package com.friendlyreminder.application.util;

public enum RelativeImportance {

    LOW(0),
    MEDIUM(1),
    HIGH(2);

    private final int relativeImportance;

    RelativeImportance(int relativeImportance) {
        this.relativeImportance = relativeImportance;
    }
}
