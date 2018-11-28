package com.friendlyreminder.application.sorter;

import com.friendlyreminder.application.person.Contact;

import java.util.Comparator;
import java.util.List;

public enum ContactListSortingStrategy implements SortingStrategy<Contact> {

    ByMostRecentContact {
        @Override
        public void sortList(List<Contact> list) {}
    },
    ByName {
        @Override
        public void sortList(List<Contact> list) {}
    },
    ByWeightedRelevance {
        @Override
        public void sortList(List<Contact> list) {}
    }
}
