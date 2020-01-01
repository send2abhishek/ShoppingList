package com.example.abhishekaryan.shoppinglist.Views.AddFrndListView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abhishekaryan.shoppinglist.Entities.user;
import com.example.abhishekaryan.shoppinglist.Infrastructure.Utils;
import com.example.abhishekaryan.shoppinglist.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addFrndListViewHolder extends RecyclerView.ViewHolder {

    private TextView userAddEmail;
    public ImageView addItemView;
    public addFrndListViewHolder(View itemView) {
        super(itemView);

        userAddEmail=(TextView)itemView.findViewById(R.id.list_shopping_list_add_name) ;
        addItemView=(ImageView)itemView.findViewById(R.id.list_user_list_itemView);
    }


    public void populate(user currentUser){

        itemView.setTag(currentUser);


        userAddEmail.setText(currentUser.getEmail());



    }
}
