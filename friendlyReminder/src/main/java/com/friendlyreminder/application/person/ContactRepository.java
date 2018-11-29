package com.friendlyreminder.application.person;

import org.springframework.data.repository.CrudRepository;

/**
 * Class that is AUTO implemented by Spring into a Bean called contactRepository that is passed to controllers
 */
public interface ContactRepository extends CrudRepository<Contact, Integer> {

}