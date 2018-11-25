package com.friendlyreminder.application.person;

import com.friendlyreminder.application.book.ContactBook;
import com.friendlyreminder.application.book.ContactBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping(path = "/users")
public class UserController {

    private final UserRepository userRepository;

    private final ContactBookRepository contactBookRepository;

    public static final String LOGGED_IN_USER_ID_SESSION_ATTRIBUTE_NAME = "loggedInUserId";


    @Autowired
    public UserController(UserRepository userRepository, ContactBookRepository contactBookRepository) {
        this.userRepository = userRepository;
        this.contactBookRepository = contactBookRepository;
    }

    @RequestMapping(path="/addBook")
    public String addContactBook(){
        return "newBook";
    }
    @PostMapping(path = "/addBook.do")
    public String addContactBookToUser(RedirectAttributes redirectAttributes, HttpSession httpSession, @RequestParam String contactBookName){
        Integer loggedInUserId = UserController.getUserIdFromSession(httpSession);
        if(loggedInUserId == null){
            redirectAttributes.addFlashAttribute("errorMessage","Not Logged in! Can't add contact book!");
            return "redirect:/home";
        }
        Optional<User> optionalUser = userRepository.findById(loggedInUserId);
        if(!optionalUser.isPresent()){
            removeLoggedInUserIdFromSession(httpSession);
            redirectAttributes.addFlashAttribute("errorMessage","An Error Occurred While Adding Contact Book!");
            return "redirect:/home";
        }
        User loggedInUser = optionalUser.get();
        ContactBook newBook = new ContactBook(contactBookName);
        contactBookRepository.save(newBook);
        loggedInUser.addContactBook(newBook);
        userRepository.save(loggedInUser);
        redirectAttributes.addFlashAttribute("successMessage","Added new contact book!");
        return "redirect:/home";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the contact list
        return userRepository.findAll();
    }

    public static Integer getUserIdFromSession(HttpSession httpSession){
        return (Integer) httpSession.getAttribute(LOGGED_IN_USER_ID_SESSION_ATTRIBUTE_NAME);
    }

    public static void setSessionLoggedInUserId(HttpSession httpSession, Integer id){
        httpSession.setAttribute(LOGGED_IN_USER_ID_SESSION_ATTRIBUTE_NAME,id);
    }

    public static void removeLoggedInUserIdFromSession(HttpSession httpSession){
        httpSession.removeAttribute(LOGGED_IN_USER_ID_SESSION_ATTRIBUTE_NAME);
    }

}
