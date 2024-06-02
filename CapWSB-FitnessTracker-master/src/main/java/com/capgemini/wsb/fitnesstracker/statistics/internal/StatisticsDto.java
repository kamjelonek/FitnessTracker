package com.capgemini.wsb.fitnesstracker.statistics.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import jakarta.annotation.Nullable;

import java.time.LocalDate;

record StatisticsDto(@Nullable Long id,
               User user,
               int totalTrainings,
               double totalDistance,
               int totalCaloriesBurned) {

}

record StatisticsDtoNoUser(@Nullable Long id,
                     int totalTrainings,
                     double totalDistance,
                     int totalCaloriesBurned) {

}
