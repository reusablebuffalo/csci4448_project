package com.friendlyreminder.application.person;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Class that is AUTO implemented by Spring into a Bean called userRepository that is passed to controllers
 */
public interface UserRepository extends CrudRepository<User, Integer> {
    // Resource for creating different types of queries
    // https://stackoverflow.com/questions/33153271/how-do-you-create-a-spring-jpa-repository-findby-query-using-a-property-that-con
    List<User> findByUsername(String username);
}
