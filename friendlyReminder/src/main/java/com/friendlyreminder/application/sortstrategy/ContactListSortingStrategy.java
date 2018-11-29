package com.friendlyreminder.application.sortstrategy;

import com.friendlyreminder.application.person.Contact;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * enum that implements the sorting strategy interface for list of {@link Contact} in multiple ways
 */
public enum ContactListSortingStrategy implements SortingStrategy<Contact> {

    /**
     * Sorts {@link List} of {@link Contact}s by decreasing date of most recent contact
     */
    ByMostRecentContact("most recent") {
        @Override
        public void sortList(List<Contact> list) {
            list.sort(Comparator.comparing(Contact::getMostRecentContactDate).reversed());
        }
    },
    /**
     * Sorts {@link List} of {@link Contact}s by increasing date of most recent contact
     */
    ByLeastRecentContact("least recent") {
        @Override
        public void sortList(List<Contact> list) {
            list.sort(Comparator.comparing(Contact::getMostRecentContactDate));
        }
    },
    /**
     * Sorts {@link List} of {@link Contact}s by lastname then firstname in alphabetical order
     */
    ByNameAscending("name ascending") {
        @Override
        public void sortList(List<Contact> list) {
            list.sort(Comparator.comparing(Contact::getLastName)
                                .thenComparing(Contact::getFirstName));
        }
    },
    /**
     * Sorts {@link List} of {@link Contact}s by lastname then firstname in reverse-alphabetical order
     */
    ByNameDescending("name descending") {
        @Override
        public void sortList(List<Contact> list) {
            list.sort(Comparator.comparing(Contact::getLastName)
                    .thenComparing(Contact::getFirstName)
                    .reversed());
        }
    },
    /**
     * Sorts {@link List} of {@link Contact}s by lastname then firstname in alphabetical order
     */
    ByWeightedRelevance("Order of Recommended Contact") {
        @Override
        public void sortList(List<Contact> list) {
            list.sort(Comparator.comparing((Contact contact)-> DAYS.between(contact.getMostRecentContactDate(), LocalDate.now())*contact.getRelativeImportance().relativeImportance)
                    .reversed());
        }
    };

    private String alternateStrategyName;

    ContactListSortingStrategy(String alternateStrategyName){
        this.alternateStrategyName = alternateStrategyName;

    }

    public String getAlternateStrategyName(){
        return alternateStrategyName;
    }
}
