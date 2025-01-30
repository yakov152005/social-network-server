package org.server.socialnetworkserver.jobs;
import org.server.socialnetworkserver.entitys.User;
import org.server.socialnetworkserver.repositoris.UserRepository;
import org.server.socialnetworkserver.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.server.socialnetworkserver.utils.ApiEmailProcessor.sendEmail;
import static org.server.socialnetworkserver.utils.Constants.EmailConstants.TITLE;

@Component
public class InactiveUsersJob {
    private final UserRepository userRepository;
    private final StringBuilder CONTENT;

    @Autowired
    public InactiveUsersJob(UserRepository userRepository){
        this.userRepository = userRepository;
        this.CONTENT = new StringBuilder();
        CONTENT.append(Constants.EmailConstants.CONTENT).append("\n")
                .append("Link: ").append("https://social-network-client-k8fp.onrender.com").append(" 👈").append("\n")
                .append("🙂 !נתראה");
    }


     @Scheduled(fixedRate = 1000)
    public void printMessage() {
        System.out.println("Job is running: " + new Date());
    }


    @Scheduled(cron = "0 * 11 * * *")
    public void sendMailToNotLoggedUsers(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);

        Date lastWeek = calendar.getTime();
        List<User> loginActivityList = userRepository.findUsersNotLoggedInLastWeek(lastWeek);

        if (loginActivityList != null){
            List<String> emails = loginActivityList.stream().map(User::getEmail).toList();

            for (String email : emails){
                System.out.println(email);
                String username = userRepository.findUsernameByEmail(email);

                sendEmail(email, TITLE + username + "?" , CONTENT.toString());
            }
        }
    }
}
