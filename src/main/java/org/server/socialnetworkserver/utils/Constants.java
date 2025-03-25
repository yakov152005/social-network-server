package org.server.socialnetworkserver.utils;
import io.github.cdimascio.dotenv.Dotenv;


public class Constants {

    public static Dotenv dotenv = Dotenv.load();

    public static class ForTest{
        // public static final String URL_TEST = dotenv.get("URL_TEST");
        public static final String URL_TEST = "https://social-network-server-m0ef.onrender.com/slow-endpoint";
    }

    public static class ChatGpt{
        public static final String ID_GPT = "206263667";
        public static final String SEND_MESSAGE = "https://app.seker.live/fm1/send-message";
    }


    public static class UrlClient{
        public static final String URL_SERVER = "/social-network";
        // public static final String URL_CLIENT_PC = dotenv.get("URL_CLIENT_PC");
        public static final String URL_CLIENT_PC = "https://social-network-client-k8fp.onrender.com";
    }

    public static class EmailConstants{
        // public static final String SENDER_EMAIL = dotenv.get("SENDER_EMAIL");
       // public static final String SENDER_PASSWORD = dotenv.get("SENDER_PASSWORD");
        public static final String PERSONAL = "Social Network";
        public static final String TITLE = "מה קורה ";
        public static final String CONTENT = "💟 לא התחברת המון זמן, בוא לבקר אותנו ";
        public static final String[] EMAILS_CONTAINS = {"walla.co.il", "walla.com", "gmail.com", "gmail.co.il", "edu.aac.ac.il"};
    }

    public static class SmsConstants{
        public static final String SMS_SENDER = "NETWORK-API";
        //public static final String SMS_TOKEN = dotenv.get("SMS_TOKEN");
        public static final String URL_SMS = "https://capi.inforu.co.il/api/v2/SMS/SendSms";
    }

    public static class Notification {
        public static final String FOLLOW = "follow";
        public static final String LIKE = "like";
        public static final String COMMENT = "comment";
    }

    public static class Errors{
        public static final String ERROR_1 = "Failed: please fill all filed.";
        public static final String ERROR_2 = "Failed: This username is exist, please choose a another name.";
        public static final String ERROR_3 =
                String.join("\n",
                "Failed: The password must be 8 characters long, contain \n" +
                "at least one letter, and at least one special character -> "
                ,HelpMethodConstants.SPECIAL_CHAR );
        public static final String ERROR_4 = "Failed: The password confirm  you entered does not match the original password.' ";
        public static final String ERROR_5 = "Failed: Phone number must start with 05 and be exactly 10 digits long.";
        public static final String ERROR_6 = "Failed: This Phone number is exist, please choose another phone number.";
        public static final String ERROR_7 = "Failed: This email is exist, please choose another email.";
        public static final String ERROR_8 = "Failed: The email must be in the style 'example@yourmail.com OR .co.il' ";

        public static final int NO_ERROR = 1;
        public static final int ERROR_USER = 2;
        public static final int ERROR_PASSWORD = 3;
        public static final int ERROR_CONFIRM_PASSWORD = 4;
        public static final int ERROR_PHONE = 5;
        public static final int ERROR_EMAIL = 6;
    }

    public static class HelpMethodConstants{
        public static final String SPECIAL_CHAR = "!@#$%^&*()-+=_";
        public static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        public static final int LENGTH_PASSWORD = 8;
        public static final int LENGTH_PHONE = 10;
        public static final String START_WITH_PHONE = "05";
    }


    public static class LOCAL_HOST{
        public static final String SENDER_EMAIL = dotenv.get("SENDER_EMAIL");
        public static final String SENDER_PASSWORD = dotenv.get("SENDER_PASSWORD");

        public static final String SMS_TOKEN = dotenv.get("SMS_TOKEN");
        public static final String URL_SMS = "https://capi.inforu.co.il/api/v2/SMS/SendSms";

        public static final String URL_CLIENT_LAPTOP = dotenv.get("URL_CLIENT_LAPTOP");
        public static final String URL_CLIENT_PC = dotenv.get("URL_CLIENT_PC");

        public static final String URL_TEST = dotenv.get("URL_TEST");

        public static final String ID_GPT = dotenv.get("ID_GPT");
        public static final String SEND_MESSAGE = dotenv.get("PATH_SEND_MESSAGE");
        /*
         עם משתני סביבה למסד נתונים לוקאלי
        spring.datasource.url=${DB_URL_LOCAL}
        spring.datasource.username=${DB_USERNAME_LOCAL}
        spring.datasource.password=${DB_PASSWORD_LOCAL}
        spring.datasource.name=${DB_NAME_LOCAL}
         */

        /*
        עם משתני סביבה למסד נתונים חיצוני
        spring.datasource.url=${DB_URL}
        spring.datasource.username=${DB_USERNAME}
        spring.datasource.password=${DB_PASSWORD}
        spring.datasource.name=${DB_NAME}
         */
    }
}
