package com.friendlyreminder.application.person;

import com.friendlyreminder.application.contactbook.ContactBook;
import com.friendlyreminder.application.utility.InvalidPasswordException;
import com.friendlyreminder.application.utility.InvalidUsernameException;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent a User of the application
 * - handles login credentials (username and password)
 * - contains zero or more {@link ContactBook}s
 */
@Entity // This tells Hibernate to make a table out of this class
@Table(name = "users")
public class User extends Person {

    private String username;

    private String password;

    @OneToMany
    private List<ContactBook> contactBookList;

    /**
     * Default constructor, initializes members with parent's ({@link Person}) defaults
     * initializes contactBookList too empty list
     */
    private User(){ // this default constructor should never be called since we require that Users have username and password
        super();
        contactBookList = new ArrayList<>();
    }

    /**
     * Constructs User object with a username and password
     * @param username username (can't be empty)
     * @param password password (can't be empty)
     * @throws InvalidUsernameException thrown if username is empty
     * @throws InvalidPasswordException thrown if password is empty
     */
    public User(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
        this();
        if(StringUtils.isEmpty(username)) throw new InvalidUsernameException("username can't be empty");
        if(StringUtils.isEmpty(password)) throw new InvalidPasswordException("password can't be empty");
        setUsername(username);
        changePassword(password,password);
    }

    /**
     * Method to check if password matches member password (successful login), as password is private and has no getter
     * @param password password to check this.password against
     * @return true if password is correct, if not matched (or null) returns false
     */
    public Boolean validatePassword(String password){
        if (this.password == null) return false;
        return this.password.equals(password);
    }

    /**
     * Method to change password of User
     * @param oldPassword current password, can be null if instance does not yet have password
     * @param newPassword password to set
     * @return true if change is successful otherwise false
     */
    public Boolean changePassword(String oldPassword, String newPassword){
        if (this.password == null) {
            this.password = newPassword;
            return true;
        } else if (validatePassword(oldPassword)){
            this.password = newPassword;
            return true;
        } else {
            return false; // can't change password
        }
    }

    /**
     * adds new ContactBook to list
     * @param newContactBook {@link ContactBook} to add
     */
    public void addContactBook(ContactBook newContactBook){
        contactBookList.add(newContactBook);
    }

    /**
     * Method to remove all instances of {@link ContactBook} in contactBookList with this identifier
     * @param contactBookId {@link Integer} unique id of book to remove
     */
    public void removeContactBook(Integer contactBookId){
        contactBookList.removeIf(contactBook -> contactBook.getId().equals(contactBookId));
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
