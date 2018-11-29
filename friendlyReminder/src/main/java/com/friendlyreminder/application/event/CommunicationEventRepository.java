package com.friendlyreminder.application.event;

import org.springframework.data.repository.CrudRepository;

/**
 * Class that is AUTO implemented by Spring into a Bean called communicationEventRepository that is passed to controllers
 */
public interface CommunicationEventRepository extends CrudRepository<CommunicationEvent, Integer> {

}