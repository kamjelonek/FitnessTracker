package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
class UserController {

    private final UserServiceImpl userService;

    private final UserMapper userMapper;
    private final UserMapperPart userMapperPart;
    private final UserMapperIdEmail userMapperIdEmail;

    @GetMapping
    public List<UserDtoPart> getAllUsers() {

        log.info("log.info -> userService.findAllUsers");

        return userService.findAllUsers()
                          .stream()
                          .map(userMapperPart::toDtoPart)
                          .collect(Collectors.toList());
    }

    @GetMapping("/email/{email}")
    public List<UserDtoIdEmail> getUserByEmail(@PathVariable final String email) {

        return userService.getUserByEmail(email)
                          .stream()
                          .map(userMapperIdEmail::toDtoIdEmail)
                          .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public List<UserDto> getUserById(@PathVariable final Long id) {

        log.info("log.info -> userService.getUser");

        return userService.getUser(id)
                          .stream()
                          .map(userMapper::toDto)
                          .collect(Collectors.toList());
    }

    @GetMapping("/older-than/{age}")
    public List<UserDto> getUsersOlderThan(@PathVariable final String age) {

        log.info("log.info -> userService.getUsersOlderThan");

        return userService.getUsersOlderThan(age)
                          .stream()
                          .map(userMapper::toDto)
                          .collect(Collectors.toList());
    }

    // do usuniecia ???
    /*
    @GetMapping("/delete-by-id/{Id}")
    public ResponseEntity<Void>  deleteUserById(@PathVariable final Long Id) {

        log.info("log.info -> userService.deleteUserById = " + Id);
        userService.deleteUserById(Id);

        return ResponseEntity.noContent().build();

    }
    */

    // Windows curl -X POST "http://localhost:8080/v1/users" -H "Content-Type: application/json" -d "{\"firstName\": \"Zenobiusz\", \"lastName\": \"Nazwisko\", \"birthdate\": \"1990-01-02\", \"email\": \"example@example.com\"}"
    @PostMapping
    public ResponseEntity<User>  addUser(@RequestBody UserDto userDto) {

        log.info("log.info -> userService.addUser");

        //System.out.println("User with firstName: " + userDto.firstName() + " passed to the request");
        //System.out.println("User with lastName: " + userDto.lastName() + " passed to the request");
        //System.out.println("User with birthdate: " + userDto.birthdate() + " passed to the request");
        //System.out.println("User with e-mail: " + userDto.email() + " passed to the request");

        User newUser =  userService.createUser (new User(
                userDto.firstName(),
                userDto.lastName(),
                userDto.birthdate(),
                userDto.email()));

        //URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        //        .path("/{id}")
        //        .buildAndExpand(newUser.getId())
        //        .toUri();

        URI location = URI.create("/v1/users/" + newUser.getId());

        return ResponseEntity.created(location).body(newUser);
    }

    @GetMapping("/delete-by-id/{id}")
    public ResponseEntity<Void>  deleteUserById(@PathVariable final Long id) {

        log.info("log.info -> userService.deleteUserById = {}", id);
        userService.deleteUserById(id);

        return ResponseEntity.noContent().build();
    }

    // Windows curl -X DELETE http://localhost:8080/v1/users/11
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>  delUser(@PathVariable final Long id) {

        log.info("@DeleteMapping -> log.info -> userService.deleteUserById = {}", id);
        userService.deleteUserById(id);

        return ResponseEntity.noContent().build();
    }

    // Windows curl -X PUT  http://localhost:8080/v1/users/11 -H "Content-Type: application/json" -d "{\"firstName\": \"NoweImie\", \"lastName\": \"NoweNazwisko\", \"birthdate\": \"2000-01-02\", \"email\": \"nowe@example.com\"}"
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable final Long id, @RequestBody UserDto userDto) {

        log.info("log.info -> userService.updateUser = {}", id);
        User updatedUser = userService.updateUser(id, userMapper.toEntity(userDto));
        return ResponseEntity.ok(userMapper.toDto(updatedUser));

    }
}
