package com.friendlyreminder.application.contactbook;

import com.friendlyreminder.application.person.Contact;
import com.friendlyreminder.application.sortstrategy.ContactListSortingStrategy;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a contact book. It contains a list of {@link Contact}s
 * - This class is saved to Relational Database according to Hibernate annotations
 * - The contact list in the contact book is sorted by one of {@link ContactListSortingStrategy} strategies
 */
@Entity // This tells Hibernate to make a table out of this class
@Table(name = "contactbooks")
public class ContactBook {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String name;

    @OneToMany
    private List<Contact> contactList;

    @Enumerated
    private ContactListSortingStrategy sortingStrategy;

    /**
     * Constructs ContactBook with a name
     * @param name {@link String} name of ContactBook
     */
    public ContactBook(String name){
        this();
        setName(name);
    }

    /**
     * Default constructor for ContactBook
     * - default name = ""
     * - initializes contactList to empty list
     * - sets default {@link ContactListSortingStrategy} to {@literal ByWeightedRelevance}
     *
     */
    public ContactBook(){
        setName("");
        contactList = new ArrayList<>();
        setSortingStrategy(ContactListSortingStrategy.ByWeightedRelevance);
    }

    /**
     * Adds new contact to list of contacts and then sorts the list according to current {@link ContactListSortingStrategy}
     * @param newContact {@link Contact} to add to this ContactBook's list of contacts
     */
    public void addContact(Contact newContact){
        contactList.add(newContact);
        sortingStrategy.sortList(contactList);
    }

    /**
     * Remove contact (identified by unique id) from list. If more than one contact with same id, both are removed
     * @param contactId {@link Integer} unique id for contact for be removed from contactList
     */
    public void removeContact(Integer contactId){
        contactList.removeIf(contact -> contact.getId().equals(contactId));
    }

    // getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Contact> getContactList() {
        sortingStrategy.sortList(contactList);
        return contactList;
    }

    public ContactListSortingStrategy getSortingStrategy() {
        return sortingStrategy;
    }

    public void setSortingStrategy(ContactListSortingStrategy sortingStrategy) {
        this.sortingStrategy = sortingStrategy;
    }
}
