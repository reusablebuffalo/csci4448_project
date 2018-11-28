package com.friendlyreminder.application.util;

public enum RelativeImportance {

    LOW(0),
    MEDIUM(1),
    HIGH(2);

    private final int relativeImportance;

    /**
     * Constructs appropriate enum from integer value between 1 and 3
     * @param relativeImportance integer value between 1 and 3, that indicates (by increasing value) the relative importance of object
     */
    RelativeImportance(int relativeImportance) {
        this.relativeImportance = relativeImportance;
    }

    public int getRelativeImportance() {
        return relativeImportance;
    }
}
