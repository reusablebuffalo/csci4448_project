package com.friendlyreminder.application.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "books")
public class ContactBookController {

//    private final ContactBookRepository contactBookRepository;
//
//    @Autowired
//    public ContactBookController(ContactBookRepository contactBookRepository) {
//        this.contactBookRepository = contactBookRepository;
//    }
//
//    @GetMapping(path="/all")
//    public @ResponseBody Iterable<ContactBook> getAllContactBooks() {
//        // This returns a JSON or XML with the contact list
//        return contactBookRepository.findAll();
//    }
}
