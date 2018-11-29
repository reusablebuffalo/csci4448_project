package com.friendlyreminder.application.person;

import com.friendlyreminder.application.event.CommunicationEvent;
import com.friendlyreminder.application.sortstrategy.CommunicationEventSortingStrategy;
import com.friendlyreminder.application.utility.RelativeImportance;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Class that represents a contact. It contains a list of {@link CommunicationEvent}s
 * - This class is saved to Relational Database according to Hibernate annotations
 * - The event list in the contact is sorted by one of {@link CommunicationEventSortingStrategy} strategies
 */
@Entity // This tells Hibernate to make a table out of this class
@Table(name = "contacts")
public class Contact extends Person {

    private String phoneNumber;

    @Enumerated(EnumType.ORDINAL)
    private RelativeImportance relativeImportance;

    @OneToMany
    private List<CommunicationEvent> communicationEvents;

    @Enumerated
    private CommunicationEventSortingStrategy sortingStrategy;

    private LocalDate mostRecentContactDate;

    /**
     * Default constructor for Contact.
     * Sets default sortingStrategy {@link CommunicationEventSortingStrategy} to {@code ByDateDescending}
     * Initializes communication events to empty list
     */
    public Contact(){
        super();
        this.sortingStrategy = CommunicationEventSortingStrategy.ByDateDescending;
        this.communicationEvents = new ArrayList<>();
    }

    public Contact(String firstName, String lastName, String emailAddress, String notes, String phoneNumber, RelativeImportance relativeImportance){
        super(firstName, lastName, emailAddress, notes);
        this.sortingStrategy = CommunicationEventSortingStrategy.ByDateDescending;
        this.communicationEvents = new ArrayList<>();
        setPhoneNumber(phoneNumber);
        setRelativeImportance(relativeImportance);
    }

    /**
     * This method adds a communication event to a contact's list of communication events
     * then sorts the list using the class's {@link CommunicationEventSortingStrategy}, which can be set/changed
     * @param event CommunicationEvent to add to this contact
     */
    public void addCommunicationEvent(CommunicationEvent event){
        if(mostRecentContactDate == null){
            setMostRecentContactDate(event.getEventDate());
        }else if(event.getEventDate().isAfter(getMostRecentContactDate())){
            setMostRecentContactDate(event.getEventDate());
        }
        communicationEvents.add(event);
        sortingStrategy.sortList(communicationEvents);
    }

    /**
     * Finds last date this contact was communicated with
     * @return last date of contact as (MM-DD-YYYY) or "n/a" if never been contacted
     */
    public String getLastContactDate(){
        if(mostRecentContactDate == null){
            return "n/a";
        }
        return mostRecentContactDate.toString();
    }

    /**
     * Removes specific communication event from this contact's list of communication events
     * @param eventId unique id of the communication event to remove from list of events
     */
    public void removeCommunicationEvent(Integer eventId){
        communicationEvents.removeIf(event -> event.getId().equals(eventId));
        if(communicationEvents.isEmpty()){
            mostRecentContactDate = null;
        } else {
            mostRecentContactDate = Collections.max(communicationEvents, Comparator.comparing(CommunicationEvent::getEventDate)).getEventDate();
        }
    }

    public List<CommunicationEvent> getCommunicationEvents() {
        sortingStrategy.sortList(communicationEvents);
        return communicationEvents;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public RelativeImportance getRelativeImportance() {
        return relativeImportance;
    }

    public void setRelativeImportance(RelativeImportance relativeImportance) {
        this.relativeImportance = relativeImportance;
    }

    public CommunicationEventSortingStrategy getSortingStrategy() {
        return sortingStrategy;
    }

    public void setSortingStrategy(CommunicationEventSortingStrategy sortingStrategy) {
        this.sortingStrategy = sortingStrategy;
    }

    /**
     * method to return most recent date that this contact was contacted
     * @return {@link LocalDate} corresponding to most recent contact date, or if null {@link LocalDate}.MIN
     */
    public LocalDate getMostRecentContactDate() {
        if(mostRecentContactDate == null){
            return LocalDate.MIN; // default value (required when comparing)
        }
        return mostRecentContactDate;
    }

    public void setMostRecentContactDate(LocalDate mostRecentContactDate) {
        this.mostRecentContactDate = mostRecentContactDate;
    }
}