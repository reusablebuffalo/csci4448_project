package com.friendlyreminder.application.sortstrategy;

import java.util.List;

// interface for sorting strategy

/**
 * interface that defines sorting strategy contract (sort list)
 * @param <E> type of list that this strategy will sort
 */
public interface SortingStrategy<E> {
    /**
     * Generic list sorting method for {@link List} of type {@literal <E>}. SortingStrategies must implement this method.
     * @param list generic {@link List} to be sorted
     */
    void sortList(List<E> list);
}
