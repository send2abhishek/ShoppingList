package com.example.abhishekaryan.shoppinglist.live;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.abhishekaryan.shoppinglist.Entities.Users;
import com.example.abhishekaryan.shoppinglist.Infrastructure.ShoppingListApplication;
import com.example.abhishekaryan.shoppinglist.Services.GetUserService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.otto.Subscribe;

public class LiveUsersService extends BaseLiveService {
    public LiveUsersService(ShoppingListApplication application) {
        super(application);
    }


    @Subscribe
    public void getUsersFriends(final GetUserService.getUSerFriendRequest request){

        final GetUserService.getUSerFriendResponse response=new GetUserService.getUSerFriendResponse();

        response.listener=request.reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                response.userFrnd=dataSnapshot.getValue(Users.class);
                bus.post(response);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


                Toast.makeText(application.getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT);

            }
        });


    }
}
