package com.friendlyreminder.application.person;

import com.friendlyreminder.application.book.ContactBook;
import com.friendlyreminder.application.book.ContactBookRepository;
import com.friendlyreminder.application.util.RelativeImportance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/users")
public class UserController {

    private final UserRepository userRepository;

    private final ContactBookRepository contactBookRepository;

    private final ContactRepository contactRepository;

    public static final String LOGGED_IN_USER_ID_SESSION_ATTRIBUTE_NAME = "loggedInUserId";


    @Autowired
    public UserController(UserRepository userRepository, ContactBookRepository contactBookRepository, ContactRepository contactRepository) {
        this.userRepository = userRepository;
        this.contactBookRepository = contactBookRepository;
        this.contactRepository = contactRepository;
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

    @GetMapping(path = "/deleteBook")
    public String deleteBook(@RequestParam Integer contactBookId, HttpSession httpSession, RedirectAttributes redirectAttributes){
        Integer loggedInUserId = UserController.getUserIdFromSession(httpSession);
        if(loggedInUserId == null){
            redirectAttributes.addFlashAttribute("errorMessage","Not Logged in! Can't delete contact book!");
            return "redirect:/home";
        }
        Optional<User> optionalUser = userRepository.findById(loggedInUserId);
        if(!optionalUser.isPresent()){
            removeLoggedInUserIdFromSession(httpSession);
            redirectAttributes.addFlashAttribute("errorMessage","An Error Occurred While Deleting Contact Book!");
            return "redirect:/home";
        }
        User loggedInUser = optionalUser.get();
        loggedInUser.removeContactBook(contactBookId);
        userRepository.save(loggedInUser);
        contactBookRepository.deleteById(contactBookId);
        redirectAttributes.addFlashAttribute("successMessage", "Deleted contact book!");
        return "redirect:/home";
    }

    @GetMapping(path = "/openBook")
    public String openBook(@RequestParam Integer contactBookId, HttpSession httpSession, RedirectAttributes redirectAttributes, Model model){
        Integer loggedInUserId = UserController.getUserIdFromSession(httpSession);
        if(loggedInUserId == null){
            redirectAttributes.addFlashAttribute("errorMessage","Not Logged in! Can't delete contact book!");
            return "redirect:/home";
        }
        Optional<User> optionalUser = userRepository.findById(loggedInUserId);
        if(!optionalUser.isPresent()){
            removeLoggedInUserIdFromSession(httpSession);
            redirectAttributes.addFlashAttribute("errorMessage","An Error Occurred While Deleting Contact Book!");
            return "redirect:/home";
        }
        User loggedInUser = optionalUser.get();
        Optional<ContactBook> optionalContactBook = loggedInUser.getContactBookById(contactBookId);
        if(!optionalContactBook.isPresent()){
            redirectAttributes.addFlashAttribute("errorMessage","An Error Occurred While Deleting Contact Book!");
            return "redirect:/home";
        }
        ContactBook contactBook = optionalContactBook.get();
        model.addAttribute("firstName",loggedInUser.getFirstName());
        model.addAttribute("contactBook",contactBook);
        return "contactBook";
    }

    @RequestMapping(path="/addContact")
    public String addContact(Model model, @RequestParam Integer contactBookId){
        model.addAttribute("contactBookId",contactBookId);
        model.addAttribute("roles",RelativeImportance.values());
        model.addAttribute("newContact", true);
        return "addContact";
    }

    @RequestMapping(path="/updateContact")
    public String updateContact(Model model, @RequestParam Integer contactId, @RequestParam Integer contactBookId){
        Optional<Contact> optionalContact = contactRepository.findById(contactId);
        if(optionalContact.isPresent()) {
            Contact contact = optionalContact.get();
            model.addAttribute("contact", contact);
        }
        model.addAttribute("contactBookId", contactBookId);
        model.addAttribute("contactId", contactId);
        model.addAttribute("roles",RelativeImportance.values());
        model.addAttribute("newContact", false);
        return "addContact";
    }

    @PostMapping(path = "/updateContact.do")
    public String addContactToContactBook(RedirectAttributes redirectAttributes, HttpSession httpSession, Model model,
                                          @RequestParam Integer contactBookId,
                                          @RequestParam RelativeImportance importance,
                                          @RequestParam String contactFirstName,
                                          @RequestParam String contactLastName,
                                          @RequestParam String contactNotes,
                                          @RequestParam String contactPhoneNumber,
                                          @RequestParam String contactEmailAddress,
                                          @RequestParam Integer contactId){
        Optional<Contact> optionalContact = contactRepository.findById(contactId);
        if(optionalContact.isPresent()){
            Contact contact = optionalContact.get();
            contact.setFirstName(contactFirstName);
            contact.setLastName(contactLastName);
            contact.setRelativeImportance(importance);
            contact.setPhoneNumber(contactPhoneNumber);
            contact.setEmailAddress(contactEmailAddress);
            contact.setNotes(contactNotes);
            contactRepository.save(contact);
            model.addAttribute("successMessage", "Contact updated!");
        } else {
            model.addAttribute("errorMessage","Error Updating Contact!");
        }
        return openBook(contactBookId, httpSession, redirectAttributes, model);
    }

    @PostMapping(path = "/addContact.do")
    public String addContactToContactBook(RedirectAttributes redirectAttributes, HttpSession httpSession, Model model,
                                          @RequestParam Integer contactBookId,
                                          @RequestParam RelativeImportance importance,
                                          @RequestParam String contactFirstName,
                                          @RequestParam String contactLastName,
                                          @RequestParam String contactNotes,
                                          @RequestParam String contactPhoneNumber,
                                          @RequestParam String contactEmailAddress){
        Optional<ContactBook> optionalContactBook = contactBookRepository.findById(contactBookId);
        if(optionalContactBook.isPresent()){
            ContactBook contactBook = optionalContactBook.get();
            Contact newContact = new Contact();
            newContact.setFirstName(contactFirstName);
            newContact.setLastName(contactLastName);
            newContact.setRelativeImportance(importance);
            newContact.setPhoneNumber(contactPhoneNumber);
            newContact.setEmailAddress(contactEmailAddress);
            newContact.setNotes(contactNotes);
            contactBook.addContact(newContact);
            contactRepository.save(newContact);
            contactBookRepository.save(contactBook);
            model.addAttribute("successMessage", "Contact added!");
        } else {
            model.addAttribute("errorMessage","Error Updating Contact Book!");
        }
        return openBook(contactBookId,httpSession, redirectAttributes, model);
    }

    @GetMapping(path = "/deleteContact")
    public String deleteBook(@RequestParam Integer contactBookId, @RequestParam Integer contactId, HttpSession httpSession, RedirectAttributes redirectAttributes, Model model){
        Optional<ContactBook> contactBookOptional = contactBookRepository.findById(contactBookId);
        if(contactBookOptional.isPresent()){
            ContactBook contactBook = contactBookOptional.get();
            contactBook.removeContact(contactId);
            contactBookRepository.save(contactBook);
        }
        contactRepository.deleteById(contactId);
        model.addAttribute("successMessage", "Deleted contact!");
        return openBook(contactBookId,httpSession,redirectAttributes,model);
    }

    @GetMapping(path = "/openContact")
    public String viewContact(@RequestParam Integer contactId,
                              @RequestParam Integer contactBookId,
                              Model model, RedirectAttributes redirectAttributes, HttpSession httpSession){
        Optional<Contact> contactOptional = contactRepository.findById(contactId);
        if(contactOptional.isPresent()) {
            Contact contact = contactOptional.get();
            model.addAttribute("contact",contact);
            model.addAttribute("contactBookId", contactBookId);
            return "contact";
        } else {
            model.addAttribute("errorMessage","Something went wrong when trying to view contact!");
            return openBook(contactBookId,httpSession,redirectAttributes,model);
        }
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
