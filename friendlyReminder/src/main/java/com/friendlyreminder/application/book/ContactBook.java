package com.friendlyreminder.application.book;

import com.friendlyreminder.application.person.Contact;
import com.friendlyreminder.application.sorter.ContactListSorter;
import com.friendlyreminder.application.sorter.WeightedRelevanceContactListSorter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "contactbooks")
public class ContactBook {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String name;

    @OneToMany
    private List<Contact> contactList;

//    @Transient // we don't need to persist this
//    private transient ContactListSorter contactListSorter;

    public ContactBook(String name){
        this();
        this.name = name;
    }

    public ContactBook(){
        contactList = new ArrayList<>();
//        contactListSorter = new WeightedRelevanceContactListSorter(); // default sorter
    }

    public void addContact(Contact newContact){
        contactList.add(newContact);
//        contactList = contactListSorter.sortList(contactList); // sort after adding
    }

    public void removeContact(Integer id){
        contactList.removeIf(contact -> contact.getId().equals(id));
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
        return contactList;
    }

//    public ContactListSorter getContactListSorter() {
//        return contactListSorter;
//    }
//
//    public void setContactListSorter(ContactListSorter contactListSorter) {
//        this.contactListSorter = contactListSorter;
//    }
}
