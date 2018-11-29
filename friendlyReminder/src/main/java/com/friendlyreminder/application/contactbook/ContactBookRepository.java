package com.friendlyreminder.application.contactbook;

import org.springframework.data.repository.CrudRepository;

/**
 * Class that is AUTO implemented by Spring into a Bean called contactBookRepository that is passed to controllers
 */
public interface ContactBookRepository extends CrudRepository<ContactBook, Integer> {
}
