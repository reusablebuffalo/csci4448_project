package com.friendlyreminder.application.person;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called studentRepository
// CRUD refers Create, Read, Update, Delete

public interface ContactRepository extends CrudRepository<Contact, Integer> {

    // Resource for creating different types of queries
    // https://stackoverflow.com/questions/33153271/how-do-you-create-a-spring-jpa-repository-findby-query-using-a-property-that-con
    List<Contact> findByFirstName(String firstName);
    List<Contact> findByFirstNameAndLastName(String firstName, String lastName);
}