package com.friendlyreminder.application.utility;

public enum RelativeImportance {

    /**
     * Low importance
     */
    LOW(1),
    /**
     * Medium importance
     */
    MEDIUM(5),
    /**
     * High importance
     */
    HIGH(10);

    public final Long relativeImportance;

    /**
     * Constructs appropriate enum from integer value in [1,5,10]
     * @param relativeImportance integer value in [1,5,10], that indicates (by increasing value) the relative importance of object
     */
    RelativeImportance(long relativeImportance) {
        this.relativeImportance = relativeImportance;
    }
}
