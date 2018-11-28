package com.friendlyreminder.application.sorter;

import java.util.List;

// interface for sorting strategy
public interface SortingStrategy<E> {
    /**
     * Generic list sorting method for {@link List} of type {@literal <E>}. SortingStrategies must implement this method.
     * @param list generic {@link List} to be sorted
     */
    void sortList(List<E> list);
}
