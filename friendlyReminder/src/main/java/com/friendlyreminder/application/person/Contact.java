package com.friendlyreminder.application.person;

import com.friendlyreminder.application.event.CommunicationEvent;
import com.friendlyreminder.application.sorter.CommunicationEventSortingStrategy;
import com.friendlyreminder.application.util.RelativeImportance;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
        communicationEvents.add(event);
        sortingStrategy.sortList(communicationEvents);
    }

    /**
     * Finds last date this contact was communicated with
     * @return last date of contact as (MM-DD-YYYY) or "n/a" if never been contacted
     */
    public String getLastContactDate(){
        if(getCommunicationEvents().isEmpty()){
            return "n/a";
        }
        CommunicationEventSortingStrategy.ByDateDescending.sortList(getCommunicationEvents());
        String lastContactDate = getCommunicationEvents().get(0).getDateAsString();
        sortingStrategy.sortList(getCommunicationEvents());
        return lastContactDate;
    }

    /**
     * Removes specific communication event from this contact's list of communication events
     * @param eventId unique id of the communication event to remove from list of events
     */
    public void removeCommunicationEvent(Integer eventId){
        communicationEvents.removeIf(event -> event.getId().equals(eventId));
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
}