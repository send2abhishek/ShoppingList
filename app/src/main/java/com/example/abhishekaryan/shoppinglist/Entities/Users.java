package com.example.abhishekaryan.shoppinglist.Entities;

import java.util.HashMap;

public class Users {

    private HashMap<String,user> usersFriends;


    public Users() {
    }

    public Users(HashMap<String, user> usersFriends) {

        this.usersFriends = usersFriends;

    }

    public void setUsersFriends(String key ,user current) {
        usersFriends.put(key,current);
    }

    public HashMap<String, user> getUsersFriends() {
        return usersFriends;
    }
}
