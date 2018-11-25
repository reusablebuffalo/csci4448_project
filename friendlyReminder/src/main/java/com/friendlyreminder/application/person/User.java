package com.friendlyreminder.application.person;

import com.friendlyreminder.application.book.ContactBook;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "users")
public class User extends Person {

    private String username;

    private String password;

    @OneToMany
    private List<ContactBook> contactBookList;

    public User(){
        contactBookList = new ArrayList<>();
    }

    public User(String username, String password){
        contactBookList = new ArrayList<>();
        setUsername(username);
        changePassword(password,password);
    }

    public Boolean validatePassword(String password){
        return this.password.equals(password);
    }

    public Boolean changePassword(String oldPassword, String newPassword){
        if (this.password == null) {
            this.password = newPassword;
            return true;
        } else if (oldPassword.equals(this.password)){
            this.password = newPassword;
            return true;
        } else {
            return false; // can't change password
        }
    }

    public void addContactBook(ContactBook newContactBook){
        contactBookList.add(newContactBook);
    }

    public void removeContactBook(Integer id){
        contactBookList.removeIf(contactBook -> contactBook.getId().equals(id));
    }

    public List<ContactBook> getContactBookList() {
        return contactBookList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
