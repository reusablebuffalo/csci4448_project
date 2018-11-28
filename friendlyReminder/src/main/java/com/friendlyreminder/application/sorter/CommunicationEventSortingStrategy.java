package com.friendlyreminder.application.sorter;

import com.friendlyreminder.application.event.CommunicationEvent;
import com.friendlyreminder.application.person.Contact;

import java.util.Comparator;
import java.util.List;

public enum CommunicationEventSortingStrategy implements SortingStrategy<CommunicationEvent> {

    ByDate {
        @Override
        public void sortList(List<CommunicationEvent> list) {
              list.sort(Comparator.comparing((CommunicationEvent event) -> event.getDateTime().getYear())
                      .thenComparing(event -> event.getDateTime().getMonth())
                      .thenComparing(event -> event.getDateTime().getDay())
                      .reversed()); // most recent first
        }
    }
}
