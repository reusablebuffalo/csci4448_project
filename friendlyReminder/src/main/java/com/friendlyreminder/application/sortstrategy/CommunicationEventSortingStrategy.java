package com.friendlyreminder.application.sortstrategy;

import com.friendlyreminder.application.event.CommunicationEvent;

import java.util.Comparator;
import java.util.List;

/**
 * enum that implements the sorting strategy interface for list of {@link CommunicationEvent} in multiple ways
 */
public enum CommunicationEventSortingStrategy implements SortingStrategy<CommunicationEvent> {
    /**
     * Sorts {@link List} of {@link CommunicationEvent}s by decreasing date
     */
    ByDateDescending("descending") {
        @Override
        public void sortList(List<CommunicationEvent> list) {
              list.sort(Comparator.comparing((CommunicationEvent event) -> event.getEventDate().getYear())
                      .thenComparing(event -> event.getEventDate().getMonth())
                      .thenComparing(event -> event.getEventDate().getDayOfMonth())
                      .reversed()); // most recent last
        }
    },
    /**
     * Sorts {@link List} of {@link CommunicationEvent}s by increasing date
     */
    ByDateAscending("ascending") {
        @Override
        public void sortList(List<CommunicationEvent> list) {
            list.sort(Comparator.comparing((CommunicationEvent event) -> event.getEventDate().getYear())
                    .thenComparing(event -> event.getEventDate().getMonth())
                    .thenComparing(event -> event.getEventDate().getDayOfMonth()));
        }
    };

    private String alternateStrategyName;

    CommunicationEventSortingStrategy(String alternateStrategyName){
        this.alternateStrategyName = alternateStrategyName;

    }

    public String getAlternateStrategyName(){
        return alternateStrategyName;
    }
}
