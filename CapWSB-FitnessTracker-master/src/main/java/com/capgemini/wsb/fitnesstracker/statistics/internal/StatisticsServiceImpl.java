package com.capgemini.wsb.fitnesstracker.statistics.internal;

import com.capgemini.wsb.fitnesstracker.statistics.api.Statistics;
import com.capgemini.wsb.fitnesstracker.statistics.api.StatisticsProvider;
import com.capgemini.wsb.fitnesstracker.statistics.api.StatisticsService;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
class StatisticsServiceImpl implements StatisticsService, StatisticsProvider {

    private final StatisticsRepository statisticsRepository;

    @Override
    public Statistics createStatistics(final Statistics statistics) {
        log.info("Creating Statistics {}", statistics);
        if (statistics.getId() != null) {
            throw new IllegalArgumentException("Statistics has already DB ID, update is not permitted!");
        }
        return statisticsRepository.save(statistics);
    }

    @Override
    public Optional<Statistics> getStatistics(final Long statisticsId) {
        return statisticsRepository.findById(statisticsId);
    }


    @Override
    public void deleteStatisticsById(Long id){
        log.info("log.info -> statisticsService.deleteStatisticsById {}", id);
        statisticsRepository.deleteById(id);
    };


    //@Override
    public Statistics updateStatistics(Long  Id, User user, Statistics statisticsUpdates) {

        Statistics statistics = statisticsRepository.findById(Id)
                .orElseThrow(() -> new UserNotFoundException(Id));

        log.info("log.info -> StatisticsService.updateStatistics = {}", Id);
        statistics.update(
                user,
                statisticsUpdates.getTotalTrainings(),
                statisticsUpdates.getTotalDistance(),
                statisticsUpdates.getTotalCaloriesBurned());

        return statisticsRepository.save(statistics);
    }

    @Override
    public List<Statistics> getStatisticsByUserId(Long id) {
        return List.of();
    }

    ;


    //@Override
    public List<Statistics> findAllStatistics() {
        return statisticsRepository.findAll();
    }

}