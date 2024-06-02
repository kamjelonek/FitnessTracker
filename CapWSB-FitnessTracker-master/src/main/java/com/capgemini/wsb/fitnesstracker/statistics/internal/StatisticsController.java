package com.capgemini.wsb.fitnesstracker.statistics.internal;

import com.capgemini.wsb.fitnesstracker.statistics.api.Statistics;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.internal.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/v1/statistics")
@RequiredArgsConstructor
class StatisticsController {

    private final StatisticsServiceImpl statisticsService;
    private final StatisticsMapper statisticsMapper;
    private final UserServiceImpl userService;

    @GetMapping
    public List<StatisticsDto> getAllStatistics() {

        log.info("log.info -> statisticsService.findAllStatistics");

        return statisticsService.findAllStatistics()
                                .stream()
                                .map(statisticsMapper::toDto)
                                .collect(Collectors.toList());
    }


    @GetMapping("/{id}")
    public List<StatisticsDto> getStatisticsById(@PathVariable final Long id) {

        log.info("log.info -> statisticsService.getStatistics");

        return statisticsService.getStatistics(id)
                                .stream()
                                .map(statisticsMapper::toDto)
                                .collect(Collectors.toList());
    }


    @GetMapping("/delete-by-id/{id}")
    public ResponseEntity<Void> deleteStatisticsById(@PathVariable final Long id) {

        log.info("log.info -> statisticsService.deleteStatisticsById = {}", id);
        statisticsService.deleteStatisticsById(id);

        return ResponseEntity.noContent().build();
    }


    // Windows curl -X DELETE http://localhost:8080/v1/statistics/11
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>  delStatistics(@PathVariable final Long id) {

        log.info("@DeleteMapping -> log.info -> statisticsService.deleteStatistics = {}", id);
        statisticsService.deleteStatisticsById(id);

        return ResponseEntity.noContent().build();
    }


    @PostMapping
    public ResponseEntity<Statistics> addStatistics(@RequestBody StatisticsDto statisticsDto) {

        log.info("log.info -> statisticsService.addStatistics");
        Optional<User> user = userService.getUser(statisticsDto.user().getId());
        log.info("log.info -> statisticsService.addStatistics = id = {}", statisticsDto.user().getId());

        /*
        Statistics newStatistics =  statisticsService.createStatistics (new Statistics(
                statisticsDto.user(),
                statisticsDto.totalTrainings(),
                statisticsDto.totalDistance(),
                statisticsDto.totalCaloriesBurned()));
        */

        Statistics newStatistics;
        if (user.isEmpty()) {
            User newUser = null;
            try {
                newUser = userService.createUser(new User(
                        statisticsDto.user().getFirstName(),
                        statisticsDto.user().getLastName(),
                        statisticsDto.user().getBirthdate(),
                        statisticsDto.user().getEmail()));
            }   catch (Exception ignored) {}

            newStatistics = statisticsService.createStatistics(new Statistics(
                    newUser,
                    statisticsDto.totalTrainings(),
                    statisticsDto.totalDistance(),
                    statisticsDto.totalCaloriesBurned()));
        } else {
            newStatistics = statisticsService.createStatistics(new Statistics(
                    statisticsDto.user(),
                    statisticsDto.totalTrainings(),
                    statisticsDto.totalDistance(),
                    statisticsDto.totalCaloriesBurned()));
        }

        //URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        //        .path("/{id}")
        //        .buildAndExpand(newStatistics.getId())
        //        .toUri();

        URI location = URI.create("/v1/statistics" + newStatistics.getId());

        return ResponseEntity.created(location).body(newStatistics);
    }


    // Windows curl -X PUT  http://localhost:8080/v1/statistics/10 -H "Content-Type: application/json" -d "{\"id\": \"10\", \"totalTrainings\": \"5\", \"totalDistance\": \"1234.1\", \"totalCaloriesBurned\": \"999\"}"
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStatistics(@PathVariable Long id, @RequestBody StatisticsDtoNoUser statisticsDtoNoUser) {

        log.info("log.info -> userService.updateUser statisticsDtoNoUser.id() = {}", statisticsDtoNoUser.id());

        if (statisticsDtoNoUser.id() == null){
            return new ResponseEntity<>("ID cannot be null", HttpStatus.BAD_REQUEST);
        }

        if (!statisticsDtoNoUser.id().equals(id)){
            return new ResponseEntity<>("IDs are not equal", HttpStatus.BAD_REQUEST);
        }

        Statistics updatedStatistics = statisticsService.updateStatistics(statisticsDtoNoUser.id(), null, statisticsMapper.toEntityNoUser(statisticsDtoNoUser));
        return ResponseEntity.ok(statisticsMapper.toDtoNoUser(updatedStatistics));

    }

}