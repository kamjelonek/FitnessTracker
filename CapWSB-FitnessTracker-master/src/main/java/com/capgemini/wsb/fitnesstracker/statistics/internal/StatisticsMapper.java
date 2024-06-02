package com.capgemini.wsb.fitnesstracker.statistics.internal;

import com.capgemini.wsb.fitnesstracker.statistics.api.Statistics;
import org.springframework.stereotype.Component;

@Component
class StatisticsMapper {

    StatisticsDto toDto(Statistics statistics) {
        return new StatisticsDto(statistics.getId(),
                           statistics.getUser(),
                           statistics.getTotalTrainings(),
                           statistics.getTotalDistance(),
                           statistics.getTotalCaloriesBurned());
    }

    Statistics toEntity(StatisticsDto statisticsDto) {
        return new Statistics(
                        statisticsDto.user(),
                        statisticsDto.totalTrainings(),
                        statisticsDto.totalDistance(),
                        statisticsDto.totalCaloriesBurned());
    }


    StatisticsDtoNoUser toDtoNoUser(Statistics statistics) {
        return new StatisticsDtoNoUser(statistics.getId(),
                statistics.getTotalTrainings(),
                statistics.getTotalDistance(),
                statistics.getTotalCaloriesBurned());
    }


    Statistics toEntityNoUser(StatisticsDtoNoUser statisticsDtoNoUser) {
        return new Statistics(
                statisticsDtoNoUser.totalTrainings(),
                statisticsDtoNoUser.totalDistance(),
                statisticsDtoNoUser.totalCaloriesBurned());
    }

}
