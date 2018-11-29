package com.friendlyreminder.application.event;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * class that represents a communication event object that includes notes, communication type and a date
 * annotated with {@link javax.persistence} annotations for use with Hibernate
 */
@Entity
@Table(name = "communicationevents")
public class CommunicationEvent {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String note;

    private String communicationType;

    private LocalDate eventDate;

    /**
     * private access-only used by Hibernate in entity creation
     */
    private CommunicationEvent (){} // require dateTime, note, communicationType

    /**
     * CommunicationEvent constructor that builds a communication event
     * @param eventDate {@link LocalDate} date that event occurred
     * @param note notes about this event, general information
     * @param communicationType enumerable, specifies type of communication that defines this event
     */
    public CommunicationEvent(LocalDate eventDate, String note, String communicationType){
        setEventDate(eventDate);
        setNote(note);
        setCommunicationType(communicationType);
    }

    /**
     * method to convert eventDate to string by calling .toString() method of {@link LocalDate}
     * @return eventDate as string formatted date (yyyy-mm-dd)
     */
    public String getDateAsString(){
        return eventDate.toString();
    }

    // getters and setters
    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCommunicationType() {
        return communicationType;
    }

    public void setCommunicationType(String communicationType) {
        this.communicationType = communicationType;
    }
}
