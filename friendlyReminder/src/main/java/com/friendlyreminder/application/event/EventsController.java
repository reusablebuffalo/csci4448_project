package com.friendlyreminder.application.event;

import com.friendlyreminder.application.event.CommunicationEvent;
import com.friendlyreminder.application.event.CommunicationEventRepository;

import com.friendlyreminder.application.person.Contact;
import com.friendlyreminder.application.person.ContactRepository;
import com.friendlyreminder.application.util.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(path="/events") // This means URL's start with /contacts
public class EventsController {

    // This means to get the bean called contactRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private final ContactRepository contactRepository;
    private final CommunicationEventRepository communicationEventRepository;

    @Autowired
    public EventsController(ContactRepository contactRepository, CommunicationEventRepository communicationEventRepository) {
        this.contactRepository = contactRepository;
        this.communicationEventRepository = communicationEventRepository;
    }

    @GetMapping(path="/add")
    public @ResponseBody String addCommunicationEventToContact (@RequestParam String firstName
            , @RequestParam String lastName, @RequestParam String note
            , @RequestParam String communicationType) {

        List<Contact> contactsFound = contactRepository.findByFirstNameAndLastName(firstName, lastName);
        if (contactsFound.isEmpty()){
            return "Contact with that firstName and lastName not found";
        } else if(contactsFound.size() > 1) {
            return "More than one contact with that firstName and lastName found";
        } else {
            CommunicationEvent new_event = new CommunicationEvent(new DateTime(), note, communicationType);
            communicationEventRepository.save(new_event);
            contactsFound.get(0).addCommunicationEvent(new_event);
            contactRepository.save(contactsFound.get(0));
            return "Event saved for " + contactsFound.get(0).toString(); // eventually will redirect back to home after add
        }
    }


    @GetMapping(path="/all")
    public @ResponseBody Iterable<CommunicationEvent> getAllCommunicationEvents() {
        // This returns a JSON or XML with the contact list
        return communicationEventRepository.findAll();
    }
}