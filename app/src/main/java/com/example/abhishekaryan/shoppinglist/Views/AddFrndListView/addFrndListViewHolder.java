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
    private ImageView addItemView;
    public addFrndListViewHolder(View itemView) {
        super(itemView);

        userAddEmail=(TextView)itemView.findViewById(R.id.list_shopping_list_add_name) ;
        addItemView=(ImageView)itemView.findViewById(R.id.list_user_list_itemView);
    }


    public void populate(final user currentUser, final String CurrentUserEmail, final Context context){


        userAddEmail.setText(currentUser.getEmail());
        addItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(CurrentUserEmail.equals(currentUser.getEmail())){

                    Toast.makeText(context,"You cannnot add yourself to frnd",Toast.LENGTH_SHORT).show();
                }
                else {

                    DatabaseReference reference=FirebaseDatabase.getInstance()
                            .getReferenceFromUrl(Utils.FIREBASE_ADD_FRND_REFERNCE + Utils.encodeEmail(CurrentUserEmail)
                            + "/" + Utils.encodeEmail(currentUser.getEmail()));


                    reference.setValue(currentUser);

                    addItemView.setImageResource(R.mipmap.ic_bought);


                }
            }
        });
    }
}
