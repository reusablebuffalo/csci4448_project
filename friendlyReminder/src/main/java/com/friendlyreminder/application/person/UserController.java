package com.friendlyreminder.application.person;

import com.friendlyreminder.application.HomeController;
import com.friendlyreminder.application.contactbook.ContactBook;
import com.friendlyreminder.application.contactbook.ContactBookRepository;
import com.friendlyreminder.application.event.CommunicationEvent;
import com.friendlyreminder.application.event.CommunicationEventRepository;
import com.friendlyreminder.application.sortstrategy.CommunicationEventSortingStrategy;
import com.friendlyreminder.application.sortstrategy.ContactListSortingStrategy;
import com.friendlyreminder.application.utility.CommunicationType;
import com.friendlyreminder.application.utility.RelativeImportance;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * controller class that handles displaying all views for a logged-in user
 * controller handles requests to add/update/delete {@link ContactBook}s,{@link Contact}s, {@link CommunicationEvent}s, and
 * {@link com.friendlyreminder.application.sortstrategy.SortingStrategy}s
 */
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

    /**
     * controller method to display new {@link ContactBook} creation page
     * @return filename of view that displays new contact page
     */
    @RequestMapping(path="/addBook")
    public String addContactBook(){
        return "newBook";
    }

    /**
     * method that performs actual creation of new {@link ContactBook}
     * @param redirectAttributes contains attributes used by controller in redirect scenario
     * @param httpSession session that will contain login status as attribute
     * @param model model object with model attributes used in displaying view
     * @param contactBookName name of created contactbook, passed as POST request param
     * @return filename of view to display (if still logged in, displays User home page else landing page)
     */
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

    /**
     * method that handles deletion of a {@link ContactBook} by id
     * @param redirectAttributes contains attributes used by controller in redirect scenario
     * @param httpSession session that will contain login status as attribute
     * @param model model object with model attributes used in displaying view
     * @param contactBookId unique id of contactbook to delete
     * @return filename of view for homepage
     */
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

    /**
     * method to handle changing {@link ContactBook} sortingStrategy
     * @param redirectAttributes contains attributes used by controller in redirect scenario
     * @param httpSession session that will contain login status as attribute
     * @param model model object with model attributes used in displaying view
     * @param contactBookId unique id of contactbook
     * @param sortingStrategy strategy to sort this ContactBook with
     * @return filename of view to display
     */
    @GetMapping(path = "/openBook/changeSort")
    public String changeContactSort(HttpSession httpSession, RedirectAttributes redirectAttributes, Model model,
                                    @RequestParam Integer contactBookId,
                                    @RequestParam ContactListSortingStrategy sortingStrategy){
        Optional<User> optionalUser = getLoggedInUserFromSession(httpSession);
        if(!optionalUser.isPresent()){
            return authenticationErrorRedirect(redirectAttributes);
        }
        Optional<ContactBook> optionalContactBook = contactBookRepository.findById(contactBookId);
        if(optionalContactBook.isPresent()){
            ContactBook contactBook = optionalContactBook.get();
            contactBook.setSortingStrategy(sortingStrategy);
            contactBookRepository.save(contactBook);
            redirectAttributes.addFlashAttribute("successMessage","sorting strategy changed!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage","sorting strategy not changed!");
        }
        redirectAttributes.addAttribute("contactBookId",contactBookId);
        return "redirect:/users/openBook";
    }

    /**
     * method to verify login and display a contactbook's contents
     * @param redirectAttributes contains attributes used by controller in redirect scenario
     * @param httpSession session that will contain login status as attribute
     * @param model model object with model attributes used in displaying view
     * @param contactBookId unique id of contact book to open
     * @return filename of view to display
     */
    @GetMapping(path = "/openBook")
    public String viewBook(HttpSession httpSession, RedirectAttributes redirectAttributes, Model model,
                           @RequestParam Integer contactBookId){
        Optional<User> optionalUser = getLoggedInUserFromSession(httpSession);
        if(!optionalUser.isPresent()){
            return authenticationErrorRedirect(redirectAttributes);
        }
        User loggedInUser = optionalUser.get();
        Optional<ContactBook> optionalContactBook = contactBookRepository.findById(contactBookId);
        if(!optionalContactBook.isPresent()){
            redirectAttributes.addFlashAttribute("errorMessage","An Error Occurred While Deleting Contact Book!");
            return "redirect:/home";
        }
        ContactBook contactBook = optionalContactBook.get();
        model.addAttribute("sortStrategies",ContactListSortingStrategy.values());
        model.addAttribute("firstName",loggedInUser.getFirstName());
        model.addAttribute("contactBook",contactBook);
        return "contactBook";
    }

    /**
     * method to display view that allows User to create new {@link Contact} for ContactBook
     * @param redirectAttributes contains attributes used by controller in redirect scenario
     * @param httpSession session that will contain login status as attribute
     * @param model model object with model attributes used in displaying view
     * @param contactBookId unique id of book to add contact to
     * @return filename of view to display
     */
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

    /**
     * method to display view that allows User to make request to update existing {@link Contact} in ContactBook
     * @param redirectAttributes contains attributes used by controller in redirect scenario
     * @param httpSession session that will contain login status as attribute
     * @param model model object with model attributes used in displaying view
     * @param contactBookId unique id of book to add contact to
     * @param contactId unique id of contact to update
     * @return filename of view to display
     */
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

    /**
     * method to perform actual update of {@link Contact} in {@link ContactBook}
     * @param redirectAttributes contains attributes used by controller in redirect scenario
     * @param httpSession session that will contain login status as attribute
     * @param model model object with model attributes used in displaying view
     * @param contactBookId unique id of contact book to update contact in
     * @param importance relative importance value of the contact
     * @param contactFirstName new first name of contact
     * @param contactLastName new last name of contact
     * @param contactNotes new notes for contact
     * @param contactPhoneNumber new phonenumber for contact
     * @param contactEmailAddress new emailaddress for contact
     * @param contactId contact to update
     * @return filename of view to display
     */
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

    /**
     * method to perform actual addtion of {@link Contact} to {@link ContactBook}
     * @param redirectAttributes contains attributes used by controller in redirect scenario
     * @param httpSession session that will contain login status as attribute
     * @param model model object with model attributes used in displaying view
     * @param contactBookId unique id of contact book to update contact in
     * @param importance relative importance value of the contact
     * @param contactFirstName new first name of contact
     * @param contactLastName new last name of contact
     * @param contactNotes new notes for contact
     * @param contactPhoneNumber new phonenumber for contact
     * @param contactEmailAddress new emailaddress for contact
     * @return filename of view to display
     */
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
            contactRepository.save(newContact);
            contactBook.addContact(newContact);
            contactBookRepository.save(contactBook);
            model.addAttribute("successMessage", "Contact added!");
        } else {
            model.addAttribute("errorMessage","Error Updating Contact Book!");
        }
        return viewBook(httpSession, redirectAttributes, model, contactBookId);
    }

    /**
     * method to delete to contact from specified contactbook
     * @param redirectAttributes contains attributes used by controller in redirect scenario
     * @param httpSession session that will contain login status as attribute
     * @param model model object with model attributes used in displaying view
     * @param contactBookId contactbook to delete from
     * @param contactId contact to delete
     * @return filename of view to display (if logged in it will be contactBook view page otherwise landing page)
     */
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

    /**
     * method to change sortingStrategy for events in specified contact
     * @param redirectAttributes contains attributes used by controller in redirect scenario
     * @param httpSession session that will contain login status as attribute
     * @param model model object with model attributes used in displaying view
     * @param contactId unique id of contact to change strategy for
     * @param contactBookId book to return to if goback option is selected
     * @param sortingStrategy new strategy ({@link CommunicationEventSortingStrategy}
     * @return filename of view to display (if logged in, we display contact view page else we display landing page)
     */
    @GetMapping(path = "/openContact/changeSort")
    public String changeEventSort(HttpSession httpSession, RedirectAttributes redirectAttributes, Model model,
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

    /**
     * method to display view to see data for a given contact
     * @param redirectAttributes contains attributes used by controller in redirect scenario
     * @param httpSession session that will contain login status as attribute
     * @param model model object with model attributes used in displaying view
     * @param contactId unique id of contact to view
     * @param contactBookId book that contains this contact
     * @return filename of view to display
     */
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

    /**
     * method to display view that contains form to create new communication event for a given contact in a given contact book
     * @param redirectAttributes contains attributes used by controller in redirect scenario
     * @param httpSession session that will contain login status as attribute
     * @param model model object with model attributes used in displaying view
     * @param contactId unique id of contact to add event for
     * @param contactBookId unique id of contact book to add to
     * @return filename of view to display
     */
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

    /**
     * method that handles POST request to actually add new communication event to given contact
     * @param redirectAttributes contains attributes used by controller in redirect scenario
     * @param httpSession session that will contain login status as attribute
     * @param model model object with model attributes used in displaying view
     * @param contactId id of contact to add event to
     * @param contactBookId id of contact book this contact belongs to
     * @param date parameter (passed from form) that designates which date this occurred on
     * @param eventType parameter (passed from form) that designates type of contact (enum {@link CommunicationType}
     * @param eventNotes parameter (passed from form) that is notes related to this event, designated by user
     * @return filename of view to display after transaction completed (landing if not logged in, contact view page if successful)
     */
    @PostMapping(path = "/addEvent.do")
    public String addEventToContact(HttpSession httpSession, RedirectAttributes redirectAttributes, Model model,
                                    @RequestParam Integer contactId,
                                    @RequestParam Integer contactBookId,
                                    @RequestParam(name = "eventDate") String date,
                                    @RequestParam String eventType,
                                    @RequestParam String eventNotes){
        Optional<User> optionalUser = getLoggedInUserFromSession(httpSession);
        if(!optionalUser.isPresent()){
            return authenticationErrorRedirect(redirectAttributes);
        }
        String[] yearMonthDayStrings = StringUtils.tokenizeToStringArray(date,"-");
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

        LocalDate eventDate = LocalDate.of(yearMonthDayInt.get(0),yearMonthDayInt.get(1),yearMonthDayInt.get(2));
        Contact contactToAddEventTo = contactOptional.get();
        CommunicationEvent newEvent = new CommunicationEvent(eventDate,eventNotes,eventType);
        communicationEventRepository.save(newEvent);
        contactToAddEventTo.addCommunicationEvent(newEvent);
        contactRepository.save(contactToAddEventTo);
        model.addAttribute("successMessage","Successfully added event!");
        return viewContact(httpSession,redirectAttributes,model,contactId,contactBookId);
    }

    /**
     * method that completes GET request to delete a communication event from a contact
     * @param redirectAttributes contains attributes used by controller in redirect scenario
     * @param httpSession session that will contain login status as attribute
     * @param model model object with model attributes used in displaying view
     * @param eventId unique id of event to delete
     * @param contactId unique id of contact to delete event from
     * @param contactBookId unique id of book this contact belongs to
     * @return filename of view for controller to display
     */
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
