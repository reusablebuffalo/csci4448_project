package com.friendlyreminder.application.event;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Calendar;

@Entity
@Table(name = "communicationevents")
public class CommunicationEvent {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String note;

    private String communicationType;

    private Calendar calendarDate;

    private CommunicationEvent (){} // require dateTime, note, communicationType

    public CommunicationEvent(Calendar calendarDate, String note, String communicationType){
        setCalendarDate(calendarDate);
        setNote(note);
        setCommunicationType(communicationType);
    }

    public String getDateAsString(){
        return String.format("%s-%s-%s",calendarDate.get(Calendar.MONTH),calendarDate.get(Calendar.DAY_OF_MONTH),calendarDate.get(Calendar.YEAR));
    }

    public Calendar getCalendarDate() {
        return calendarDate;
    }

    public void setCalendarDate(Calendar calendarDate) {
        this.calendarDate = calendarDate;
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
