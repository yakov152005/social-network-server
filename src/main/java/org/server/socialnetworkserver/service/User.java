package org.server.socialnetworkserver.service;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User{
    @Id
    private String username;
    private String password;
    private String phoneNumber;
    private int age;

    public User(String username, String password, String phoneNumber, int age) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.age = age;
    }


    public User() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}


