package org.server.socialnetworkserver.utils;

import jakarta.xml.bind.DatatypeConverter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

import static org.server.socialnetworkserver.utils.Constants.HelpMethodConstants.LETTERS;
import static org.server.socialnetworkserver.utils.Constants.HelpMethodConstants.SPECIAL_CHAR;

public class GeneratorUtils {

    public static Random r = new Random();

    public static String generatorCode(){
        String letters = LETTERS;
        int randomLetters = r.nextInt(letters.length() -1);
        String verifyCode  = String.valueOf(letters.charAt(randomLetters));
        verifyCode += String.format("%04d",r.nextInt(1000));
        return verifyCode;
    }

    public static String generatorPassword(){
        String letters = LETTERS;
        String specialChar = SPECIAL_CHAR;
        int randomLetters = r.nextInt(letters.length() -1);
        int randomSpecialChar = r.nextInt(specialChar.length() -1);
        String verifyCode  = String.valueOf(letters.charAt(randomLetters));
        verifyCode += String.valueOf(specialChar.charAt(randomSpecialChar));
        verifyCode += String.format("%05d",r.nextInt(10000));
        return verifyCode;
    }

    public static String generateSalt() {
        return UUID.randomUUID().toString();
    }

    public static String hashPassword(String password, String salt) {
        try {
            String saltedPassword = password + salt;
            return DatatypeConverter.printHexBinary(
                    MessageDigest.getInstance("SHA-256").digest(saltedPassword.getBytes("UTF-8"))
            );
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
