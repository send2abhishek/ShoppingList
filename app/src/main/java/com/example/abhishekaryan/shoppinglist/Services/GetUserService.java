package com.example.abhishekaryan.shoppinglist.Services;

import com.example.abhishekaryan.shoppinglist.Entities.Users;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class GetUserService {


    private  GetUserService() {
    }


    public static class getUSerFriendRequest{

        public DatabaseReference reference;



        public getUSerFriendRequest(DatabaseReference reference) {
            this.reference = reference;

        }
    }

    public static class getUSerFriendResponse{

        public Users userFrnd;
        public ValueEventListener listener;
    }
}
