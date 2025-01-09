package org.server.socialnetworkserver.utils;

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

import static org.server.socialnetworkserver.utils.Constants.DataBase.*;
import static org.server.socialnetworkserver.utils.Constants.EmailConstants.*;

public class ApiEmailProcessor {
    public static boolean sendEmail(String recipient, String subject, String content) {
        final String host = "smtp.gmail.com";
        final int port = 465;

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", String.valueOf(port));
        properties.put("mail.smtp.connectiontimeout", "10000");
        properties.put("mail.smtp.timeout", "10000");
        properties.put("mail.smtp.writetimeout", "10000");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL,PERSONAL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            //message.setText(content);
            String htmlContent = "<html><body>"
                    + "<h3>" + subject + "</h3>"
                    + "<p>" + content.replace("\n", "<br>") + "</p>"
                    + "</body></html>";

            message.setContent(htmlContent, "text/html; charset=UTF-8");
            Transport.send(message);
            System.out.println("Email sent successfully to " + recipient);
            return true;
        } catch (MessagingException e) {
            System.out.println("Error sending email: " + e.getMessage());
            return false;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}

