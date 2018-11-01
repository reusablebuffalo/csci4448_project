package com.friendlyreminder.application.event;

import com.friendlyreminder.application.util.DateTime;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "communicationevents")
public class CommunicationEvent {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String note;

    private String communicationType;

    @Embedded
    private DateTime dateTime;

    public CommunicationEvent (){

    }
    public CommunicationEvent(DateTime dateTime, String note, String communicationType){
        setDateTime(dateTime);
        setNote(note);
        setCommunicationType(communicationType);
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
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
