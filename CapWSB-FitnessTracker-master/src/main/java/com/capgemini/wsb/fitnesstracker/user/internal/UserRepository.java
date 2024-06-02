package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

interface UserRepository extends JpaRepository<User, Long> {



    /**
     * Query searching users by email address. It matches by exact match.
     *
     * @param email email of the user to search
     * @return {@link Optional} containing found user or {@link Optional#empty()} if none matched
     */
    default List<User> findByEmail(String email) {
        return findAll().stream()
                        .filter(user ->  user.getEmail().toLowerCase().contains(email.toLowerCase()))
                        .toList();

    }
    //@Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(%:email%)")
    //default Optional<User> findByEmail(String email) {

    //}


    //default List<User> findByEmail(String email) {
    //    return findAll().stream()
    //            .filter(user -> user.getEmail().toLowerCase().contains(email.toLowerCase()))
    //            .collect(Collectors.toList());
    //}

    default List<Map<String, Object>> findByEmail2(String email) {
        return findAll().stream()
                .filter(user -> user.getEmail().toLowerCase().contains(email.toLowerCase()))
                .map(user -> {
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("id", user.getId());
                    userInfo.put("email", user.getEmail());
                    return userInfo;
                })
                .collect(Collectors.toList());
    }

    default List<User> getUsersOlderThan(String age) {
        int ageInt = Integer.parseInt(age);
        return findAll().stream()
                .filter(user ->  {
                    LocalDate birthdate = user.getBirthdate();
                    int userAge = Period.between(birthdate, LocalDate.now()).getYears();
                    return userAge > ageInt;
                })
                .collect(Collectors.toList());
    }
}
