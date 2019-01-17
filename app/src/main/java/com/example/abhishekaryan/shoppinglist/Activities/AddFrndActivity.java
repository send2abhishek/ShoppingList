package com.example.abhishekaryan.shoppinglist.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.abhishekaryan.shoppinglist.Entities.user;
import com.example.abhishekaryan.shoppinglist.Infrastructure.Utils;
import com.example.abhishekaryan.shoppinglist.R;
import com.example.abhishekaryan.shoppinglist.Views.AddFrndListView.addFrndListViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddFrndActivity extends BaseActivity {

    private RecyclerView recyclerViewListItem;
    private FirebaseRecyclerAdapter adapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_frnd_list);
        recyclerViewListItem = (RecyclerView) findViewById(R.id.activity_add_frnd_recylerView);
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
            protected void onBindViewHolder(@NonNull addFrndListViewHolder holder, int position, @NonNull user model) {

                holder.populate(model,userEmail,AddFrndActivity.this);

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
}
