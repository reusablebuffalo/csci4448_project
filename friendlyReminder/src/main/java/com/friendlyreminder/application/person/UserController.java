package com.friendlyreminder.application.person;

import com.friendlyreminder.application.book.ContactBook;
import com.friendlyreminder.application.book.ContactBookRepository;
import com.friendlyreminder.application.event.CommunicationEvent;
import com.friendlyreminder.application.event.CommunicationEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/users")
public class UserController {

    private final UserRepository userRepository;

    private final ContactBookRepository contactBookRepository;

    @Autowired
    public UserController(UserRepository userRepository, ContactBookRepository contactBookRepository) {
        this.userRepository = userRepository;
        this.contactBookRepository = contactBookRepository;
    }

    @GetMapping(path="/add") // Map ONLY GET Requests
    public @ResponseBody
    String addNewUser (@RequestParam String username
            , @RequestParam String password) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        User n = new User();
        n.changePassword(null, password);
        n.setUsername(username);
        userRepository.save(n);
        return "User Saved To Users"; // eventually will redirect back to home after add
    }

    @GetMapping(path = "/addBook")
    public @ResponseBody String addContactBookToUser(@RequestParam String username, @RequestParam String contactBookName){
        ContactBook newBook = new ContactBook(contactBookName);
        contactBookRepository.save(newBook);
        List<User> users = userRepository.findByUsername(username);
        if(users.isEmpty()){
            return "Could not find user with that username";
        } else {
            users.get(0).addContactBook(newBook);
            userRepository.save(users.get(0));
            return "Contact book saved to user successfully";
        }
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the contact list
        return userRepository.findAll();
    }
}
