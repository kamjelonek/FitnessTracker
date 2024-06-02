package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;

import java.util.Date;

record TrainingDto(@Nullable Long id, User user,
//record TrainingDto(@Nullable Long trainingId, Long user,
               //@JsonFormat(pattern = "hh-mm-ss : dd-MM-yyyy") Date startTime,
               //@JsonFormat(pattern = "hh-mm-ss : dd-MM-yyyy") Date endTime,
               @JsonFormat(pattern = "hh-mm-ss : yyyy-MM-dd") Date startTime,
               @JsonFormat(pattern = "hh-mm-ss : yyyy-MM-dd") Date endTime,

               ActivityType activityType,
               double distance,
               double averageSpeed) {
}


record TrainingDtoNoUser(@Nullable Long id,
              //@JsonFormat(pattern = "hh-mm-ss : dd-MM-yyyy") Date startTime,
              //@JsonFormat(pattern = "hh-mm-ss : dd-MM-yyyy") Date endTime,
              @JsonFormat(pattern = "hh-mm-ss : yyyy-MM-dd") Date startTime,
              @JsonFormat(pattern = "hh-mm-ss : yyyy-MM-dd") Date endTime,

              ActivityType activityType,
              double distance,
              double averageSpeed) {
}
