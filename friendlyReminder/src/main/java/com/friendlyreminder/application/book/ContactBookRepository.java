package com.friendlyreminder.application.book;

import org.springframework.data.repository.CrudRepository;

public interface ContactBookRepository extends CrudRepository<ContactBook, Integer> {
}
