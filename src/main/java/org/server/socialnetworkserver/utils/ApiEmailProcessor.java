package org.server.socialnetworkserver.utils;

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
            message.setFrom(new InternetAddress(SENDER_EMAIL));
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
        }
    }
}

/*

    public static void main(String[] args) {
        // processEmails();
    }

    public static void processEmails() {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://" + DB_HOST + ":3306/" + DB_NAME, DB_USER, DB_PASSWORD)) {
            System.out.println("Database connection successful!");

            while (true) {
                String query = "SELECT id, email, title, body FROM USER";
                try (PreparedStatement statement = connection.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        int recordId = resultSet.getInt("id");
                        String recipient = resultSet.getString("email");
                        String subject = resultSet.getString("title");
                        String content = resultSet.getString("body");

                        System.out.println("Processing email record ID: " + recordId);

                        if (sendEmail(recipient, subject, content)) {
                            System.out.println("Email sent successfully for record ID: " + recordId);

                            // Delete the record after successfully sending the email
                            String deleteQuery = "DELETE FROM recovery WHERE id = ?";
                            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                                deleteStatement.setInt(1, recordId);
                                deleteStatement.executeUpdate();
                                System.out.println("Record ID " + recordId + " deleted successfully.");
                            }
                        }
                    }
                }

                // Wait for 1 second before checking the database again
                Thread.sleep(1000);
            }
        } catch (SQLException e) {
            System.out.println("Database error occurred: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }
    */