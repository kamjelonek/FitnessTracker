package com.capgemini.wsb.fitnesstracker.notification.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserProvider;
import jakarta.annotation.PreDestroy;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@EnableScheduling
@Service
@Slf4j
@Data
public class ReportToEmailService {
    private static final String TITLE = "Last week's training reports";
    private static final int DELAY_BETWEEN_EMAILS_IN_SECONDS = 5;

    private final JavaMailSender javaMailSender;
    private final UserProvider userProvider;
    private final TrainingProvider trainingProvider;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    //@PostConstruct
    @Scheduled(cron = "0 * * * * *")
    public void generateReport() {
        log.info("Starting generation of training reports");
        List<User> users = userProvider.findAllUsers();
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            scheduler.schedule(() -> sendReport(user), (long) DELAY_BETWEEN_EMAILS_IN_SECONDS * i, TimeUnit.SECONDS);
        }
        log.info("Generation of training reports scheduled");
    }

    private void sendReport(User user) {
        log.info("Sending email to {}", user.getEmail());
        javaMailSender.send(createEmail(user));
    }

    private SimpleMailMessage createEmail(User user) {
        List<Training> trainings = trainingProvider.getAllTrainingsForUserId(user.getId());
        List<Training> lastWeekTrainings = filterLastWeekTrainings(trainings);

        log.info("Creating email for {}", user.getEmail());

        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(TITLE);
        email.setTo(user.getEmail());
        email.setText(createEmailText(lastWeekTrainings, trainings));

        log.info("Email created");
        return email;
    }

    private String createEmailText(List<Training> lastWeekTrainings, List<Training> trainings) {
        StringBuilder builder = new StringBuilder(String.format("""
                Last week, you undertook %s workout sessions,
                covering an overall distance of %s units.
                Up to now, you’ve accomplished %s workout sessions.
                _________________________________________________________________________________
                Below is a comprehensive summary of your workout sessions from the previous week:
                """,
                lastWeekTrainings.size(),
                lastWeekTrainings.stream().mapToDouble(Training::getDistance).sum(),
                trainings.size()
        ));

        lastWeekTrainings.forEach(training -> builder.append(String.format("""
                training start: %s
                training end: %s
                activity type: %s
                distance: %s
                average speed: %s
                _________________________________________________________________________________
                """,
                training.getStartTime(),
                training.getEndTime() == null ? "---" : training.getEndTime(),
                training.getActivityType(),
                training.getDistance(),
                training.getAverageSpeed()
        )));

        return builder.toString();
    }

    private List<Training> filterLastWeekTrainings(List<Training> trainings) {
        Date lastWeek = Date.from(returnBeginningOfLastWeek());
        Date yesterday = Date.from(returnYesterday());
        return trainings.stream()
                .filter(training -> training.getStartTime().after(lastWeek) && training.getStartTime().before(yesterday))
                .collect(Collectors.toList());
    }

    private Instant returnYesterday() {
        return LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
    }

    private Instant returnBeginningOfLastWeek() {
        return LocalDate.now().minusWeeks(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
    }

    @PreDestroy
    public void shutdownScheduler() {
        scheduler.shutdown();
    }
}
