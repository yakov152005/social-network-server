package org.server.socialnetworkserver.services;

import org.server.socialnetworkserver.entitys.User;
import org.server.socialnetworkserver.repositoris.UserRepository;

import java.util.List;


import static org.server.socialnetworkserver.utils.Constants.EmailConstants.EMAILS_CONTAINS;
import static org.server.socialnetworkserver.utils.Constants.Errors.*;
import static org.server.socialnetworkserver.utils.Constants.HelpMethodConstants.*;

public class HelpMethods {



    public static boolean checkIfNotNullData(String username,String password,String passwordConfirm,String phoneNumber,String email,int Age){
        if (username == null || username.isEmpty() ||
                password== null || password.isEmpty() ||
                passwordConfirm == null || passwordConfirm.isEmpty() ||
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
            System.out.println("Current " + currentUser + " user " + username);
            if (currentUser.equals(username)){
                return true;
            }
        }
        return false;
    }

    public static boolean checkPassword(String password){
        boolean oneLatterOrMore = oneCharOrMore(password,LETTERS);
        boolean oneSpecialCharOrMore = oneCharOrMore(password,SPECIAL_CHAR);
        return password.length() >= LENGTH_PASSWORD && oneLatterOrMore && oneSpecialCharOrMore;
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

    private static boolean confirmPasswordEquals(String password, String passwordConfirm) {
        return password.equals(passwordConfirm);
    }


    public static String checkAllFiled(User user, UserRepository userRepository){
        if (checkIfNotNullData(user.getUsername(),user.getPassword(),user.getPasswordConfirm(), user.getPhoneNumber(),user.getEmail(),user.getAge())){
            return ERROR_1;
        }

        List<String> usernameList = userRepository.findAll().stream().map(User::getUsername).toList();
        if (checkIfUserNameValid(user.getUsername(),usernameList)){
            return ERROR_2;
        }

        if (!checkPassword(user.getPassword())){
            return ERROR_3;
        }
        if (!confirmPasswordEquals(user.getPassword(),user.getPasswordConfirm())){
            return ERROR_4;
        }
        if (!isValidPhoneNumber(user.getPhoneNumber())){
            return ERROR_5;
        }

        if (!checkIfSamePhoneNumber(user,userRepository)){
            return ERROR_6;
        }

        if (checkIfEmailExist(user.getEmail(),userRepository)){
            return ERROR_7;
        }

        if (!checkEmailValid(user.getEmail())){
            return ERROR_8;
        }


        return null;
    }

    public static int errorCodeCheck(String errorCheck){
        if (errorCheck != null) {
            if (errorCheck.contains("username")) {
                return ERROR_USER;
            } else if (errorCheck.contains("password") && !errorCheck.contains("confirm")) {
                return ERROR_PASSWORD;
            } else if (errorCheck.contains("confirm") && errorCheck.contains("password")) {
                return ERROR_CONFIRM_PASSWORD;
            } else if (errorCheck.contains("Phone")) {
                return ERROR_PHONE;
            } else if (errorCheck.contains("email")) {
                return ERROR_EMAIL;
            }
        }
        return NO_ERROR;
    }

}
