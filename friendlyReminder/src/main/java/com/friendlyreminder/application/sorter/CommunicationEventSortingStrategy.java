package com.friendlyreminder.application.sorter;

import com.friendlyreminder.application.event.CommunicationEvent;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

public enum CommunicationEventSortingStrategy implements SortingStrategy<CommunicationEvent> {
    /**
     * Sorts {@link List} of {@link CommunicationEvent}s by decreasing date
     */
    ByDateDescending("descending") {
        @Override
        public void sortList(List<CommunicationEvent> list) {
              list.sort(Comparator.comparing((CommunicationEvent event) -> event.getCalendarDate().get(Calendar.YEAR))
                      .thenComparing(event -> event.getCalendarDate().get(Calendar.MONTH))
                      .thenComparing(event -> event.getCalendarDate().get(Calendar.DAY_OF_MONTH))
                      .reversed()); // most recent last
        }
    },
    /**
     * Sorts {@link List} of {@link CommunicationEvent}s by increasing date
     */
    ByDateAscending("ascending") {
        @Override
        public void sortList(List<CommunicationEvent> list) {
            list.sort(Comparator.comparing((CommunicationEvent event) -> event.getCalendarDate().get(Calendar.YEAR))
                    .thenComparing(event -> event.getCalendarDate().get(Calendar.MONTH))
                    .thenComparing(event -> event.getCalendarDate().get(Calendar.DAY_OF_MONTH)));
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
