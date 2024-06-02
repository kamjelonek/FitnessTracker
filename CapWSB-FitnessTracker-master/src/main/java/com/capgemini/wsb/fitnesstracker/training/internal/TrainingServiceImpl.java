package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingService;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingServiceImpl implements TrainingService, TrainingProvider {

    private final TrainingRepository trainingRepository;

    @Override
    public Training createTraining(final Training training) {
        log.info("Creating Training {}", training);
        if (training.getId() != null) {
            throw new IllegalArgumentException("User has already DB ID, update is not permitted!");
        }
        return trainingRepository.save(training);
    }

    @Override
    public Optional<Training> getTraining(final Long trainingId) {
        return trainingRepository.findById(trainingId);
    }

    @Override
    public List<Training> getTrainingByUserId(final Long trainingId) {
        return trainingRepository.findByUserId(trainingId);
    }

    @Override
    public List<Training> getTrainingAfterDate(Date date) {
        return trainingRepository.findTrainingAfterDate(date);
    }

    @Override
    public List<Training> getTrainingByActivities(ActivityType activityType) {
        return trainingRepository.findTrainingByActivities(activityType);
    }

    @Override
    public void deleteTrainingById(Long trainingId){
        log.info("log.info -> userService.deleteUserById " + trainingId);
        trainingRepository.deleteById(trainingId);
    }

    @Override
    public Training updateTraining(Long  idTraining, Training trainingUpdates) {

        Training training = trainingRepository.findById(idTraining)
                .orElseThrow(() -> new UserNotFoundException(idTraining));

        training.update(null,
                trainingUpdates.getStartTime(),
                trainingUpdates.getEndTime(),
                trainingUpdates.getActivityType(),
                trainingUpdates.getDistance(),
                trainingUpdates.getAverageSpeed());

        return trainingRepository.save(trainingUpdates);
    }

    @Override
    public Training updateTraining(Long  trainingId, User user, Training trainingUpdates) {

        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new UserNotFoundException(trainingId));

        training.update(
                user,
                trainingUpdates.getStartTime(),
                trainingUpdates.getEndTime(),
                trainingUpdates.getActivityType(),
                trainingUpdates.getDistance(),
                trainingUpdates.getAverageSpeed());

        return trainingRepository.save(training);
    }

    @Override
    public List<Training> findAllTrainings() { return trainingRepository.findAll(); }

    @Override
    public List<Training> getAllTrainingsForUserId(Long userId) {
        return trainingRepository.findByUserId(userId);
    }
}
