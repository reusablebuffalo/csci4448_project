package com.friendlyreminder.application.sorter;

import java.util.List;

// interface for sorting strategy
public interface Sorter<E> {

    List<E> sortList(List<E> list);
}
