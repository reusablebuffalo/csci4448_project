package com.friendlyreminder.application.event;

import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called studentRepository
// CRUD refers Create, Read, Update, Delete

public interface CommunicationEventRepository extends CrudRepository<CommunicationEvent, Integer> {

}