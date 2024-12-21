package org.server.socialnetworkserver.service;

import jakarta.xml.bind.DatatypeConverter;
import org.server.socialnetworkserver.repository.UserRepository;
import org.server.socialnetworkserver.entitys.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.server.socialnetworkserver.utils.Constants.EmailConstants.EMAILS_CONTAINS;
import static org.server.socialnetworkserver.utils.Constants.Errors.*;
import static org.server.socialnetworkserver.utils.Constants.HelpMethodConstants.*;

public class HelpMethods {

    public static Random r = new Random();

    public static int errorCodeCheck(String errorCheck){
        if (errorCheck != null) {
            if (errorCheck.contains("username")) {
                return ERROR_USER;
            } else if (errorCheck.contains("password")) {
                return ERROR_PASSWORD;
            } else if (errorCheck.contains("Phone")) {
                return ERROR_PHONE;
            } else if (errorCheck.contains("email")) {
                return ERROR_EMAIL;
            }
        }
        return NO_ERROR;
    }

    public static String generatorCode(){
        String letters = LETTERS;
        int randomLetters = r.nextInt(letters.length() -1);
        String verifyCode  = String.valueOf(letters.charAt(randomLetters));
        verifyCode += String.format("%04d",r.nextInt(1000));
        return verifyCode;
    }

    public static boolean checkIfNotNullData(String username,String password,String phoneNumber,String email,int Age){
        if (username == null || username.isEmpty() ||
                password== null || password.isEmpty() ||
                phoneNumber == null || phoneNumber.isEmpty() ||
                email == null || email.isEmpty() ||
                Age <= 0){
            return true;
        }
        return false;
    }

    public static boolean checkIfUserNameValid(String username, List<String> user){
        String currentUser = user.stream().filter(current ->  current.equals(username)).findFirst().orElse(null);
        if (currentUser!= null){
            if (currentUser.equals(username)){
                return true;
            }
        }
        return false;
    }

    public static boolean checkPassword(String password){
        boolean oneLatterOrMore = oneCharOrMore(password,LETTERS);
        boolean oneSpecialCharOrMore = oneCharOrMore(password,SPECIAL_CHAR);
        return password.length() > 6 && oneLatterOrMore && oneSpecialCharOrMore;
    }

    public static boolean oneCharOrMore(String password,String forCheck){
        String toLowerCase = forCheck.toLowerCase();
        for (int i = 0; i < password.length() ; i++) {
            char chPassword = password.charAt(i);
            for (int j = 0; j < forCheck.length() ; j++) {
                char chLetters = forCheck.charAt(j);
                char chLettersLower = toLowerCase.charAt(j);
                if (chPassword == chLetters || chPassword == chLettersLower){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() == LENGTH_PHONE && phoneNumber.startsWith(START_WITH_PHONE)) {
            for (int i = 0; i < phoneNumber.length(); i++) {
                char c = phoneNumber.charAt(i);
                if (!Character.isDigit(c)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean checkIfSamePhoneNumber(User user, UserRepository userRepository){
        User samePhone =  userRepository.findAll().stream()
                .filter(existingUser -> existingUser.getPhoneNumber().equals(user.getPhoneNumber()))
                .findAny()
                .orElse(null);
        if (samePhone != null){
            System.out.println("The phone is used by: " + samePhone.getUsername());
            return false;
        }
        return true;
    }

    public static boolean checkEmailValid(String email) {
        String[] temp = email.split("@");
        if (temp.length != 2) {
            return false;
        }
        String domain = temp[1];
        for (String validDomain : EMAILS_CONTAINS) {
            if (domain.equalsIgnoreCase(validDomain)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkIfEmailExist(String email, UserRepository userRepository){
        for (User repUser : userRepository.findAll()){
            if (email.equalsIgnoreCase(repUser.getEmail())){
                return true;
            }
        }
        return false;
    }

    public static String checkAllFiled(User user, UserRepository userRepository){
        if (checkIfNotNullData(user.getUsername(),user.getPassword(), user.getPhoneNumber(),user.getEmail(),user.getAge())){
            return ERROR_1;
        }

        List<String> usernameList = userRepository.findAll().stream().map(User::getUsername).toList();
        if (checkIfUserNameValid(user.getUsername(),usernameList)){
            return ERROR_2;
        }

        if (!checkPassword(user.getPassword())){
            return ERROR_3;
        }

        if (!isValidPhoneNumber(user.getPhoneNumber())){
            return ERROR_4;
        }

        if (!checkIfSamePhoneNumber(user,userRepository)){
            return ERROR_5;
        }

        if (checkIfEmailExist(user.getEmail(),userRepository)){
            return ERROR_6;
        }

        if (!checkEmailValid(user.getEmail())){
            return ERROR_7;
        }

        return null;
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
