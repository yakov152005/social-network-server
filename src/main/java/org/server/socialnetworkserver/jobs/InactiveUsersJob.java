package org.server.socialnetworkserver.jobs;
import org.server.socialnetworkserver.entitys.User;
import org.server.socialnetworkserver.repositoris.UserRepository;
import org.server.socialnetworkserver.utils.ApiEmailProcessor;
import org.server.socialnetworkserver.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.server.socialnetworkserver.utils.Constants.EmailConstants.TITLE;

@Component
public class InactiveUsersJob {
    private final UserRepository userRepository;
    private final StringBuilder CONTENT;
    private final ApiEmailProcessor apiEmailProcessor;

    @Autowired
    public InactiveUsersJob(UserRepository userRepository, ApiEmailProcessor apiEmailProcessor){
        this.userRepository = userRepository;
        this.CONTENT = new StringBuilder();
        CONTENT.append(Constants.EmailConstants.CONTENT).append("\n")
                .append("Link: ").append("https://social-network-client-k8fp.onrender.com").append(" 👈").append("\n")
                .append("🙂 !נתראה");
        this.apiEmailProcessor = apiEmailProcessor;
    }


    /*
      @Scheduled(fixedRate = 1000)
    public void printMessage() {
        System.out.println("Job is running: " + new Date());
    }
     */


    @Scheduled(cron = "0 0 11 1 * ?")
    public void sendMailToNotLoggedUsers(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -30);

        Date lastMonth = calendar.getTime();
        List<User> loginActivityList = userRepository.findUsersNotLoggedInLastMonth(lastMonth);

        if (!loginActivityList.isEmpty()) {
            loginActivityList.forEach(user -> {
                String email = user.getEmail();
                String username = user.getUsername();

                System.out.println(email);
                apiEmailProcessor.sendEmail(email, TITLE + username + "?", CONTENT.toString());
            });
        }
    }
}
