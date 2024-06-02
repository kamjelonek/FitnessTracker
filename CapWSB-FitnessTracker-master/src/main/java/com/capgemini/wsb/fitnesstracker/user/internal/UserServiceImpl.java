package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserNotFoundException;
import com.capgemini.wsb.fitnesstracker.user.api.UserProvider;
import com.capgemini.wsb.fitnesstracker.user.api.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserProvider {

    private final UserRepository userRepository;

    @Override
    public User createUser(final User user) {
        log.info("Creating User {}", user);
        if (user.getId() != null) {
            throw new IllegalArgumentException("User has already DB ID, update is not permitted!");
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUser(final Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<User> getUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User>  getUsersOlderThan(String age){
        log.info("log.info -> userService.getUsersOlderThan " + age);


        return userRepository.getUsersOlderThan(age);
    };

    @Override
    public void deleteUserById(Long userId){
        log.info("log.info -> userService.deleteUserById " + userId);
        userRepository.deleteById(userId);
    };

    @Override
    public User updateUser(Long userId, User userUpdates) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.update(
            userUpdates.getFirstName(),
            userUpdates.getLastName(),
            userUpdates.getBirthdate(),
            userUpdates.getEmail());
        return userRepository.save(user);
    };

    @Override
    public User updateUserFirstName(Long userId, User userUpdates) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.update(
                userUpdates.getFirstName(),
                null,
                null,
                null);
        return userRepository.save(user);
    };

    @Override
    public User updateUserLastName(Long userId, User userUpdates) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.update(
                null,
                userUpdates.getLastName(),
                null,
                null);
        return userRepository.save(user);
    };

    @Override
    public User updateUserBirthdate(Long userId, User userUpdates) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.update(
                null,
                null,
                userUpdates.getBirthdate(),
                null);
        return userRepository.save(user);
    };

    @Override
    public User updateUserEmail(Long userId, User userUpdates) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.update(
                null,
                null,
                null,
                userUpdates.getEmail());
        return userRepository.save(user);
    };

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

}
