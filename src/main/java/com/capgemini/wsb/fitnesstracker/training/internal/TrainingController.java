package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.internal.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/v1/trainings")
@RequiredArgsConstructor
class TrainingController {

    private final TrainingServiceImpl trainingService;
    private final TrainingMapper trainingMapper;
    private final UserServiceImpl userService;

    @GetMapping
    public List<TrainingDto> getAllTrainings() {

        System.out.println("trainingService.findAllTrainings");
        log.info("log.info -> trainingService.findAllTrainings");

        return trainingService.findAllTrainings()
                              .stream()
                              .map(trainingMapper::toDto)
                              .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public List<TrainingDto> getTrainingById(@PathVariable final Long id) {

        log.info("log.info -> trainingService.findAllTrainings = {}", id);

        return trainingService.getTraining(id)
                              .stream()
                              .map(trainingMapper::toDto)
                              .collect(Collectors.toList());
    }

    @GetMapping("/delete-by-id/{id}")
    public ResponseEntity<Void>  deleteTrainingById(@PathVariable final Long id) {

        log.info("log.info -> trainingService.deleteTrainingById = {}", id);
        trainingService.deleteTrainingById(id);

        return ResponseEntity.noContent().build();
    }

    // Windows curl -X DELETE http://localhost:8080/v1/trainings/10
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>  delTraining(@PathVariable final Long id) {

        log.info("@DeleteMapping -> log.info -> trainingService.deleteTraining = {}", id);
        trainingService.deleteTrainingById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{id}")
    public List<TrainingDto> getTrainingByUserId(@PathVariable final Long id) {

        log.info("log.info -> trainingService.findTrainingByUserId");

        return trainingService.getTrainingByUserId(id)
                              .stream()
                              .map(trainingMapper::toDto)
                              .collect(Collectors.toList());
    }

    @GetMapping("/after/{dateTraining}")
    public List<TrainingDto> getTrainingAfterDate(@PathVariable final String dateTraining) {

        Date date = null;
        //SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            date = format.parse(dateTraining);
        } catch (Exception e) {}

        if (date == null) {
            date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }

        return trainingService.getTrainingAfterDate(date)
                              .stream()
                              .map(trainingMapper::toDto)
                              .collect(Collectors.toList());
    }

    @GetMapping("/activities2")
    public List<String> getActivities2() {
        return Arrays.stream(ActivityType.values())
                     .map(ActivityType::getDisplayName)
                     .collect(Collectors.toList());
    }

    @GetMapping("/activities")
    public ActivityType[] getActivities() {
        return ActivityType.values();
    }

    @GetMapping("/activities/{activity}")
    public List<TrainingDto> getTrainingByActivities(@PathVariable final ActivityType activity) {

        log.info("log.info -> trainingService.findTrainingByActivities");

        return trainingService.getTrainingByActivities(activity)
                              .stream()
                              .map(trainingMapper::toDto)
                              .collect(Collectors.toList());
    }

    // Windows curl -X POST  http://localhost:8080/v1/trainings -H "Content-Type: application/json" -d "{\"id\": \"\", \"user\": {\"id\": \"9\", \"firstName\": \"\", \"lastName\": \"\", \"birthdate\": \"\", \"email\":  \"\"}, \"startTime\": \"16-11-11 : 2024-03-10\", \"endTime\": \"18-45-00 : 2024-03-10\", \"activityType\": \"CYCLING\", \"distance\": \"22.8\", \"averageSpeed\": \"77.89\"}"
    @PostMapping
    public ResponseEntity<Training> addTraining(@RequestBody TrainingDto trainingDto) {

        log.info("log.info -> trainingService.addTraining");
        Optional<User> user = userService.getUser(trainingDto.user().getId());
        log.info("log.info -> trainingService.addTraining = id = {}", trainingDto.user().getId());

/*
        Training newTraining =  trainingService.createTraining (new Training(
                trainingDto.user(),
                trainingDto.startTime(),
                trainingDto.endTime(),
                trainingDto.activityType(),
                trainingDto.distance(),
                trainingDto.averageSpeed()));
*/

        Training newTraining;
        if (user.isEmpty()) {
            User newUser = null;
            try {
                newUser = userService.createUser(new User(
                        trainingDto.user().getFirstName(),
                        trainingDto.user().getLastName(),
                        trainingDto.user().getBirthdate(),
                        trainingDto.user().getEmail()));
            }   catch (Exception ignored) {}

            newTraining = trainingService.createTraining(new Training(
                    newUser,
                    trainingDto.startTime(),
                    trainingDto.endTime(),
                    trainingDto.activityType(),
                    trainingDto.distance(),
                    trainingDto.averageSpeed()));
        } else {
            newTraining = trainingService.createTraining(new Training(
                    trainingDto.user(),
                    trainingDto.startTime(),
                    trainingDto.endTime(),
                    trainingDto.activityType(),
                    trainingDto.distance(),
                    trainingDto.averageSpeed()));
        }

        //URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        //        .path("/{id}")
        //        .buildAndExpand(newTraining.getId())
        //        .toUri();

        URI location = URI.create("/v1/trainings" + newTraining.getId());

        return ResponseEntity.created(location).body(newTraining);
    }

    // Windows curl -X PUT  http://localhost:8080/v1/trainings/10 -H "Content-Type: application/json" -d "{\"id\": \"10\", \"startTime\": \"11-05-00 : 2024-03-10\", \"endTime\": \"11-55-00 : 2024-03-10\", \"activityType\": \"CYCLING\", \"distance\": \"22.8\", \"averageSpeed\": \"28.5\"}"
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTraining(@PathVariable final Long id, @RequestBody TrainingDtoNoUser trainingDtoNoUser) {

        log.info("log.info -> trainingService.updateTraining = {}", id);

        if (trainingDtoNoUser.id() == null){
            return new ResponseEntity<>("ID cannot be null", HttpStatus.BAD_REQUEST);
        }

        if (!trainingDtoNoUser.id().equals(id)){
            return new ResponseEntity<>("IDs are not equal", HttpStatus.BAD_REQUEST);
        }

        Training updateTraining = trainingService.updateTraining(trainingDtoNoUser.id(),null, trainingMapper.toEntityNoUser(trainingDtoNoUser));
        return ResponseEntity.ok(trainingMapper.toDtoNoUser(updateTraining));
    }
}
