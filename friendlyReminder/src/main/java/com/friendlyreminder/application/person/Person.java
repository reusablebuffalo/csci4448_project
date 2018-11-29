package com.friendlyreminder.application.person;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Abstract class that represents a person object in the application
 */
@MappedSuperclass
public abstract class Person {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String firstName;

    private String lastName;

    private String emailAddress;

    private String notes;

    /**
     * Default Person constructor.
     * - firstName, lastName, emailAddress and notes all initialized to ""
     */
    public Person(){
        setFirstName("");
        setLastName("");
        setEmailAddress("");
        setNotes("");
    }

    /**
     * Constructor to build Person with designated members
     * @param firstName first name of person
     * @param lastName last name of person
     * @param emailAddress email address of person
     * @param notes notes about person
     */
    public Person(String firstName, String lastName, String emailAddress, String notes){
        setFirstName(firstName);
        setLastName(lastName);
        setEmailAddress(emailAddress);
        setNotes(notes);
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Overrides toString() method and prints firstName lastName
     * @return "firstName lastName"
     */
    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }
}
