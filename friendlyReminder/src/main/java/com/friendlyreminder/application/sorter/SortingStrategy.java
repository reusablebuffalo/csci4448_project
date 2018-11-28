package com.friendlyreminder.application.sorter;

import java.util.List;

// interface for sorting strategy
public interface SortingStrategy<E> {

    void sortList(List<E> list);
}
