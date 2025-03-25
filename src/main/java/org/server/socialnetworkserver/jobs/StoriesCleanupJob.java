package org.server.socialnetworkserver.jobs;

import jakarta.transaction.Transactional;
import org.server.socialnetworkserver.repositoris.StoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class StoriesCleanupJob {

    private final StoriesRepository storiesRepository;

    @Autowired
    public StoriesCleanupJob(StoriesRepository storiesRepository) {
        this.storiesRepository = storiesRepository;
    }



    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void deleteOldStories() {
        System.out.println("22");
        Instant now = Instant.now();
        Date cutoff = Date.from(now.minus(24, ChronoUnit.HOURS));

        int deleted = storiesRepository.deleteOldStoriesBefore(cutoff);
        System.out.println("🧹 CronJob: Deleted " + deleted + " stories created before " + cutoff);
    }




}
