package com.example.abhishekaryan.shoppinglist.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.abhishekaryan.shoppinglist.Entities.Users;
import com.example.abhishekaryan.shoppinglist.Entities.user;
import com.example.abhishekaryan.shoppinglist.Infrastructure.Utils;
import com.example.abhishekaryan.shoppinglist.R;
import com.example.abhishekaryan.shoppinglist.Services.GetUserService;
import com.example.abhishekaryan.shoppinglist.Views.AddFrndListView.addFrndListViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.otto.Subscribe;

import java.util.HashMap;

public class AddFrndActivity extends BaseActivity {

    private RecyclerView recyclerViewListItem;
    private FirebaseRecyclerAdapter adapter;
    private DatabaseReference databaseReference;
    private DatabaseReference UserFrendRequestReference;
    private ValueEventListener listener;
    private Users currentFrendRequestUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_frnd_list);
        recyclerViewListItem = (RecyclerView) findViewById(R.id.activity_add_frnd_recylerView);

        UserFrendRequestReference=FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Utils.FIREBASE_ADD_FRND_REFERNCE + Utils.encodeEmail(userEmail)

                         );

        bus.post(new GetUserService.getUSerFriendRequest(UserFrendRequestReference));
    }

    @Override
    protected void onResume() {
        super.onResume();




        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(Utils.FIREBASE_USER_REFERNCE);
        FirebaseRecyclerOptions<user> options =
                new FirebaseRecyclerOptions.Builder<user>()
                        .setQuery(databaseReference, user.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<user, addFrndListViewHolder>(options) {
            @NonNull
            @Override
            public addFrndListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(AddFrndActivity.this).inflate(R.layout.list_user, parent, false);

                return new addFrndListViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final addFrndListViewHolder holder, int position, @NonNull final user model) {

                holder.populate(model);

                if(isFriend(currentFrendRequestUser.getUsersFriends(),model)){
                    holder.addItemView.setImageResource(R.mipmap.ic_bought);

                }
                else {
                    holder.addItemView.setImageResource(R.mipmap.ic_plus);
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        if(userEmail.equals(model.getEmail())){


                            if(currentFrendRequestUser!=null){
                                Log.i("AbhishekAryan", "onClick: "+ currentFrendRequestUser.getUsersFriends().size());
                            }
                            Toast.makeText(AddFrndActivity.this,"You cannnot add yourself to frnd",Toast.LENGTH_SHORT).show();
                        }
                        else {

                            DatabaseReference reference=FirebaseDatabase.getInstance()
                                    .getReferenceFromUrl(Utils.FIREBASE_ADD_FRND_REFERNCE + Utils.encodeEmail(userEmail)
                                            + "/" + "usersFriends/" + Utils.encodeEmail(model.getEmail()));


                            if(isFriend(currentFrendRequestUser.getUsersFriends(),model)){

                                reference.removeValue();
                                holder.addItemView.setImageResource(R.mipmap.ic_plus);
                            }
                            else {

                                reference.setValue(model);

                                holder.addItemView.setImageResource(R.mipmap.ic_bought);

                           }




                        }

                    }
                });




            }
        };
        adapter.startListening();
        recyclerViewListItem.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewListItem.setAdapter(adapter);

    }


    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserFrendRequestReference.removeEventListener(listener);

    }

    @Subscribe

    public void GetUSerFrendResponse(GetUserService.getUSerFriendResponse response){

        listener=response.listener;

        if(response.userFrnd !=null){

            currentFrendRequestUser=response.userFrnd;

        }
        else {
            currentFrendRequestUser=new Users();
        }
    }


    private boolean isFriend(HashMap<String,user> currentUSerFrend,user currentUser){

        return currentUSerFrend!=null && currentUSerFrend.size()!=0 &&
                currentUSerFrend.containsKey(Utils.encodeEmail(currentUser.getEmail()));

    }
}
