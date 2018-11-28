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
    private CommunicationEventSortingStrategy communicationEventSortingStrategy;

    public Contact(){
        super();
        this.communicationEventSortingStrategy = CommunicationEventSortingStrategy.ByDate;
        this.communicationEvents = new ArrayList<>();
    }

    /**
     * This method adds a communication event to a contact's list of communication events
     * then sorts the list using the class's {@link CommunicationEventSortingStrategy}, which can be set/changed
     * @param event CommunicationEvent to add to this contact
     */
    public void addCommunicationEvent(CommunicationEvent event){
        this.communicationEvents.add(event);
        System.out.println(communicationEvents.toString());
        System.out.println(this.communicationEventSortingStrategy.toString());
        this.communicationEventSortingStrategy.sortList(this.communicationEvents);
    }

    public void removeCommunicationEvent(Integer id){
        communicationEvents.removeIf(event -> event.getId().equals(id));
    }

    public List<CommunicationEvent> getCommunicationEvents() {
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
}