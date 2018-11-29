package com.friendlyreminder.application.utility;

public enum RelativeImportance {

    /**
     * Low importance
     */
    LOW(1),
    /**
     * Medium importance
     */
    MEDIUM(2),
    /**
     * High importance
     */
    HIGH(4);

    public final Long relativeImportance;

    /**
     * Constructs appropriate enum from integer value in [1,2,4]
     * @param relativeImportance integer value in [1,2,4], that indicates (by increasing value) the relative importance of object
     */
    RelativeImportance(long relativeImportance) {
        this.relativeImportance = relativeImportance;
    }
}
