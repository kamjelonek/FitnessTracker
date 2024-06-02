package com.capgemini.wsb.fitnesstracker.training.api;

import com.capgemini.wsb.fitnesstracker.training.internal.ActivityType;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TrainingProvider {

    /**
     * Retrieves a training based on their ID.
     * If the user with given ID is not found, then {@link Optional#empty()} will be returned.
     *
     * @param trainingId id of the training to be searched
     * @return An {@link Optional} containing the located Training, or {@link Optional#empty()} if not found
     */
    Optional<Training> getTraining(Long trainingId);


    /**
     * Retrieves all users.
     *
     * @return An {@link Optional} containing the all Trainings,
     */

    List<Training> findAllTrainings();
    List<Training> getTrainingAfterDate(Date date);
    List<Training> getAllTrainingsForUserId(Long trainingId);
    //List<Training> getTrainingByUserId(Long trainingId);
    //List<Training> getTrainingByActivity(Long trainingId);
    List<Training> getTrainingByUserId(Long trainingId);
    List<Training> getTrainingByActivities(ActivityType activityType);


}
