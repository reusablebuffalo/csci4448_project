package com.friendlyreminder.application.util;

import javax.persistence.Embeddable;
import java.util.Date;

@Embeddable
public class DateTime {

    private Integer month;

    private Integer day;

    private Integer year;

    public DateTime(){

    }

    public DateTime(Integer month, Integer day, Integer year){
        setMonth(month);
        setDay(day);
        setYear(year);
    }

    /**
     *
     * @return whether or not the date is valid or not
     */
    public Boolean validDate(){
        return true;
    }

    /**
     *
     * @return distance, in number of days between this date and argument
     */
    public Integer distanceBetweenDates(DateTime date){
        return this.getDay() - date.getDay();
    }

    // getters and setters

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return String.format("%s-%s-%s",this.getMonth(),this.getDay(),this.getYear());
    }
}
