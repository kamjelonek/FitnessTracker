package com.capgemini.wsb.fitnesstracker.user.api;

/**
 * Interface (API) for modifying operations on {@link User} entities through the API.
 * Implementing classes are responsible for executing changes within a database transaction, whether by continuing an existing transaction or creating a new one if required.
 */
public interface UserService {

    User createUser(User user);
    void deleteUserById(Long userId);
    User updateUser(Long userId, User user);

    User updateUserFirstName(Long userId, User user);
    User updateUserLastName(Long userId, User user);
    User updateUserBirthdate(Long userId, User user);
    User updateUserEmail(Long userId, User user);
}
