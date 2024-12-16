package org.server.socialnetworkserver.utils;

public class Constants {

    public class DataBase{
        public static final String DB_HOST = "localhost";
        public static final String DB_USER = "root";
        public static final String DB_PASSWORD = "Rtadsuk152005";
        public static final String DB_NAME = "social_network_db";

    }

    public class EmailConstants{
        public static final String SENDER_EMAIL = "servicenetwork62@gmail.com"; // כתובת המייל
        public static final String SENDER_PASSWORD = "sinh araw dtpo vqoe"; // סיסמת האפליקציה
    }

    public class SmsConstants{
        public static final String SMS_TOKEN = "ZmVlZGJhY2sxOnlheGZxMzJpd2lnYW15bHBvMG9tNHVycjU";
        public static final String URL_SMS = "https://capi.inforu.co.il/api/v2/SMS/SendSms";
    }

    public class Errors{
        public static final String ERROR_1 = "Failed: please fill all filed.";
        public static final String ERROR_2 = "Failed: This username is valid, please choose a another name.";
        public static final String ERROR_3 = "Failed: The password must be 6 characters long," +
                " contain at least one special character -> " + HelpMethodConstants.SPECIAL_CHAR +
            ", and at least one letter.";
        public static final String ERROR_4 = "Failed: Phone number must start with 05 and be exactly 10 digits long.";
        public static final String ERROR_5 = "Failed: This phone number is exist, please choose another phone number.";
    }

    public class HelpMethodConstants{
        public static final String SPECIAL_CHAR = "!@#$%^&*";
        public static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        public static final int LENGTH_PHONE = 10;
        public static final String START_WITH_PHONE = "05";
    }
}
