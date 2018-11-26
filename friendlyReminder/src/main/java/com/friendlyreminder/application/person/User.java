package com.friendlyreminder.application.person;

import com.friendlyreminder.application.book.ContactBook;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "users")
public class User extends Person {

    private String username;

    private String password;

    @OneToMany
    private List<ContactBook> contactBookList;

    private User(){ // this default constructor should never be called since we require that Users have username and password
        super();
        contactBookList = new ArrayList<>();
    }

    public User(String username, String password){
        this();
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

    public Optional<ContactBook> getContactBookById(Integer id){
        for (ContactBook book: contactBookList
             ) { if (book.getId().equals(id)) return Optional.of(book);
        }
        return Optional.empty();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
