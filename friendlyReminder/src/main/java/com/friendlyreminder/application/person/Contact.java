package com.friendlyreminder.application.person;

import com.friendlyreminder.application.event.CommunicationEvent;
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
    private List<CommunicationEvent> communicationEvents = new ArrayList<>();

    public void addCommunicationEvent(CommunicationEvent event){
        communicationEvents.add(event);
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