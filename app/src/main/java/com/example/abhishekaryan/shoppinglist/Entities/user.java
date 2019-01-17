package com.example.abhishekaryan.shoppinglist.Entities;

import java.util.HashMap;

public class user {

    private String email;
    private String name;
    private HashMap<String,Object> dateJoined;
    private boolean hasLoggedInWithPassword;


    public user() {
    }

    public user(String email, String name, HashMap<String, Object> dateJoined, boolean hasLoggedInWithPassword) {
        this.email = email;
        this.name = name;
        this.dateJoined = dateJoined;
        this.hasLoggedInWithPassword = hasLoggedInWithPassword;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, Object> getDateJoined() {
        return dateJoined;
    }

    public boolean isHasLoggedInWithPassword() {
        return hasLoggedInWithPassword;
    }
}
