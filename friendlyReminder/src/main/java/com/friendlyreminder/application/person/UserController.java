package com.friendlyreminder.application.person;

import com.friendlyreminder.application.HomeController;
import com.friendlyreminder.application.book.ContactBook;
import com.friendlyreminder.application.book.ContactBookRepository;
import com.friendlyreminder.application.event.CommunicationEvent;
import com.friendlyreminder.application.event.CommunicationEventRepository;
import com.friendlyreminder.application.sorter.CommunicationEventSortingStrategy;
import com.friendlyreminder.application.util.CommunicationType;
import com.friendlyreminder.application.util.RelativeImportance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/users")
public class UserController {

    private final UserRepository userRepository;

    private final ContactBookRepository contactBookRepository;

    private final ContactRepository contactRepository;

    private final CommunicationEventRepository communicationEventRepository;

    @Autowired
    public UserController(UserRepository userRepository,
                          ContactBookRepository contactBookRepository,
                          ContactRepository contactRepository,
                          CommunicationEventRepository communicationEventRepository) {
        this.userRepository = userRepository;
        this.contactBookRepository = contactBookRepository;
        this.contactRepository = contactRepository;
        this.communicationEventRepository = communicationEventRepository;
    }

    @RequestMapping(path="/addBook")
    public String addContactBook(HttpSession httpSession, RedirectAttributes redirectAttributes, Model model){
        return "newBook";
    }

    @PostMapping(path = "/addBook.do")
    public String addContactBookToUser(HttpSession httpSession, RedirectAttributes redirectAttributes, Model model,
                                       @RequestParam String contactBookName){
        Optional<User> optionalUser = getLoggedInUserFromSession(httpSession);
        if(!optionalUser.isPresent()){
            return authenticationErrorRedirect(redirectAttributes);
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
    public String deleteBook(HttpSession httpSession, RedirectAttributes redirectAttributes, Model model,
                             @RequestParam Integer contactBookId){
        Optional<User> optionalUser = getLoggedInUserFromSession(httpSession);
        if(!optionalUser.isPresent()){
            return authenticationErrorRedirect(redirectAttributes);
        }
        User loggedInUser = optionalUser.get();
        loggedInUser.removeContactBook(contactBookId);
        userRepository.save(loggedInUser);
        contactBookRepository.deleteById(contactBookId);
        redirectAttributes.addFlashAttribute("successMessage", "Deleted contact book!");
        return "redirect:/home";
    }

    @GetMapping(path = "/openBook")
    public String viewBook(HttpSession httpSession, RedirectAttributes redirectAttributes, Model model,
                           @RequestParam Integer contactBookId){
        Optional<User> optionalUser = getLoggedInUserFromSession(httpSession);
        if(!optionalUser.isPresent()){
            return authenticationErrorRedirect(redirectAttributes);
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
    public String addContact(HttpSession httpSession, RedirectAttributes redirectAttributes, Model model,
                             @RequestParam Integer contactBookId){
        Optional<User> optionalUser = getLoggedInUserFromSession(httpSession);
        if(!optionalUser.isPresent()){
            return authenticationErrorRedirect(redirectAttributes);
        }
        model.addAttribute("contactBookId",contactBookId);
        model.addAttribute("roles",RelativeImportance.values());
        model.addAttribute("newContact", true);
        return "addContact";
    }

    @RequestMapping(path="/updateContact")
    public String updateContact(HttpSession httpSession, RedirectAttributes redirectAttributes, Model model,
                                @RequestParam Integer contactId, @RequestParam Integer contactBookId){
        Optional<User> optionalUser = getLoggedInUserFromSession(httpSession);
        if(!optionalUser.isPresent()){
            return authenticationErrorRedirect(redirectAttributes);
        }
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
    public String updateContactInContactBook(HttpSession httpSession, RedirectAttributes redirectAttributes, Model model,
                                          @RequestParam Integer contactBookId,
                                          @RequestParam RelativeImportance importance,
                                          @RequestParam String contactFirstName,
                                          @RequestParam String contactLastName,
                                          @RequestParam String contactNotes,
                                          @RequestParam String contactPhoneNumber,
                                          @RequestParam String contactEmailAddress,
                                          @RequestParam Integer contactId){
        Optional<User> optionalUser = getLoggedInUserFromSession(httpSession);
        if(!optionalUser.isPresent()){
            return authenticationErrorRedirect(redirectAttributes);
        }
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
        return viewBook(httpSession, redirectAttributes, model,contactBookId);
    }

    @PostMapping(path = "/addContact.do")
    public String addContactToContactBook(HttpSession httpSession, RedirectAttributes redirectAttributes, Model model,
                                          @RequestParam Integer contactBookId,
                                          @RequestParam RelativeImportance importance,
                                          @RequestParam String contactFirstName,
                                          @RequestParam String contactLastName,
                                          @RequestParam String contactNotes,
                                          @RequestParam String contactPhoneNumber,
                                          @RequestParam String contactEmailAddress){
        Optional<User> optionalUser = getLoggedInUserFromSession(httpSession);
        if(!optionalUser.isPresent()){
            return authenticationErrorRedirect(redirectAttributes);
        }
        Optional<ContactBook> optionalContactBook = contactBookRepository.findById(contactBookId);
        if(optionalContactBook.isPresent()){
            ContactBook contactBook = optionalContactBook.get();
            Contact newContact = new Contact(contactFirstName,contactLastName,contactEmailAddress,contactNotes,contactPhoneNumber,importance);
            contactBook.addContact(newContact);
            contactRepository.save(newContact);
            contactBookRepository.save(contactBook);
            model.addAttribute("successMessage", "Contact added!");
        } else {
            model.addAttribute("errorMessage","Error Updating Contact Book!");
        }
        return viewBook(httpSession, redirectAttributes, model, contactBookId);
    }

    @GetMapping(path = "/deleteContact")
    public String deleteBook(HttpSession httpSession, RedirectAttributes redirectAttributes, Model model,
                             @RequestParam Integer contactBookId,
                             @RequestParam Integer contactId){
        Optional<User> optionalUser = getLoggedInUserFromSession(httpSession);
        if(!optionalUser.isPresent()){
            return authenticationErrorRedirect(redirectAttributes);
        }
        Optional<ContactBook> contactBookOptional = contactBookRepository.findById(contactBookId);
        if(contactBookOptional.isPresent()){
            ContactBook contactBook = contactBookOptional.get();
            contactBook.removeContact(contactId);
            contactBookRepository.save(contactBook);
        }
        contactRepository.deleteById(contactId);
        model.addAttribute("successMessage", "Deleted contact!");
        return viewBook(httpSession,redirectAttributes,model,contactBookId);
    }

    @GetMapping(path = "/openContact/changeSort")
    public String changeSort(HttpSession httpSession, RedirectAttributes redirectAttributes, Model model,
                              @RequestParam Integer contactId,
                              @RequestParam Integer contactBookId,
                              @RequestParam CommunicationEventSortingStrategy sortingStrategy){
        Optional<User> optionalUser = getLoggedInUserFromSession(httpSession);
        if(!optionalUser.isPresent()){
            return authenticationErrorRedirect(redirectAttributes);
        }
        Optional<Contact> contactOptional = contactRepository.findById(contactId);
        if(contactOptional.isPresent()) {
            Contact contact = contactOptional.get();
            contact.setSortingStrategy(sortingStrategy);
            contactRepository.save(contact);
            redirectAttributes.addFlashAttribute("successMessage","sorting strategy changed!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage","sorting strategy not changed!");
        }
        redirectAttributes.addAttribute("contactId",contactId);
        redirectAttributes.addAttribute("contactBookId",contactBookId);
        return "redirect:/users/openContact#eventTable";

    }
    @GetMapping(path = "/openContact")
    public String viewContact(HttpSession httpSession, RedirectAttributes redirectAttributes, Model model,
                              @RequestParam Integer contactId,
                              @RequestParam Integer contactBookId){
        Optional<User> optionalUser = getLoggedInUserFromSession(httpSession);
        if(!optionalUser.isPresent()){
            return authenticationErrorRedirect(redirectAttributes);
        }
        Optional<Contact> contactOptional = contactRepository.findById(contactId);
        if(!contactOptional.isPresent()) {
            model.addAttribute("errorMessage","Something went wrong when trying to view contact!");
            return viewBook(httpSession,redirectAttributes,model,contactBookId);
        }
        Contact contact = contactOptional.get();
        model.addAttribute("contact",contact);
        model.addAttribute("contactBookId", contactBookId);
        model.addAttribute("sortStrategies", CommunicationEventSortingStrategy.values());
        return "contact";
    }

    @GetMapping(path = "/addEvent")
    public String addEvent(HttpSession httpSession, RedirectAttributes redirectAttributes, Model model,
                           @RequestParam Integer contactId,
                           @RequestParam Integer contactBookId){
        Optional<User> optionalUser = getLoggedInUserFromSession(httpSession);
        if(!optionalUser.isPresent()){
            return authenticationErrorRedirect(redirectAttributes);
        }
        model.addAttribute("contactId", contactId);
        model.addAttribute("contactBookId", contactBookId);
        model.addAttribute("communicationTypes", CommunicationType.values());
        return "addEvent";
    }

    @PostMapping(path = "/addEvent.do")
    public String addEventToContact(HttpSession httpSession, RedirectAttributes redirectAttributes, Model model,
                                    @RequestParam Integer contactId,
                                    @RequestParam Integer contactBookId,
                                    @RequestParam String eventDate,
                                    @RequestParam String eventType,
                                    @RequestParam String eventNotes){
        Optional<User> optionalUser = getLoggedInUserFromSession(httpSession);
        if(!optionalUser.isPresent()){
            return authenticationErrorRedirect(redirectAttributes);
        }
        String[] yearMonthDayStrings = StringUtils.tokenizeToStringArray(eventDate,"-");
        if(yearMonthDayStrings == null || yearMonthDayStrings.length != 3){
            model.addAttribute("errorMessage","Incorrect Date! Can not create event!");
            return viewContact(httpSession,redirectAttributes,model,contactId,contactBookId);
        }
        // convert to date as strings to integers
        List<Integer> yearMonthDayInt = Arrays.stream(yearMonthDayStrings).map(Integer::valueOf).collect(Collectors.toList());

        Optional<Contact> contactOptional = contactRepository.findById(contactId);
        if(!contactOptional.isPresent()){
            model.addAttribute("errorMessage","Could not add event to contact: contact does not exist!");
            return viewContact(httpSession,redirectAttributes,model,contactId,contactBookId);
        }

        Calendar calendarDate = new GregorianCalendar(yearMonthDayInt.get(0),yearMonthDayInt.get(1),yearMonthDayInt.get(2));
        Contact contactToAddEventTo = contactOptional.get();
        CommunicationEvent newEvent = new CommunicationEvent(calendarDate,eventNotes,eventType);
        communicationEventRepository.save(newEvent);
        contactToAddEventTo.addCommunicationEvent(newEvent);
        contactRepository.save(contactToAddEventTo);
        model.addAttribute("successMessage","Successfully added event!");
        return viewContact(httpSession,redirectAttributes,model,contactId,contactBookId);
    }

    @GetMapping(path = "/deleteEvent")
    public String deleteEvent(HttpSession httpSession, RedirectAttributes redirectAttributes, Model model,
                              @RequestParam Integer eventId,
                              @RequestParam Integer contactId,
                              @RequestParam Integer contactBookId){
        Optional<User> optionalUser = getLoggedInUserFromSession(httpSession);
        if(!optionalUser.isPresent()){
            return authenticationErrorRedirect(redirectAttributes);
        }

        Optional<Contact> contactOptional = contactRepository.findById(contactId);
        if(contactOptional.isPresent()){
            Contact contact = contactOptional.get();
            contact.removeCommunicationEvent(eventId);
            contactRepository.save(contact);
        }
        communicationEventRepository.deleteById(eventId);
        model.addAttribute("successMessage", "Deleted event!");
        return viewContact(httpSession,redirectAttributes,model,contactId,contactBookId);
    }

    /**
     * UserController method to display all registered users (and their nested contact books, contacts, events)
     * @return JSON response to be displayed by browser that contains all registered users and their data
     */
    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the contact list
        return userRepository.findAll();
    }

    // helper methods
    private Optional<User> getLoggedInUserFromSession(HttpSession httpSession){
        Integer loggedInUserId = HomeController.getUserIdFromSession(httpSession);
        if(loggedInUserId == null){
            return Optional.empty();
        }
        Optional<User> userOptional = userRepository.findById(loggedInUserId);

        // if not found, we log out
        if(!userOptional.isPresent()){
            HomeController.removeLoggedInUserIdFromSession(httpSession);
        }
        return userOptional;
    }

    private static String authenticationErrorRedirect(RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage","LoginStatusError: Redirected to home!");
        return "redirect:/home";
    }

}
