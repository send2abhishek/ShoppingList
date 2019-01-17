package com.example.abhishekaryan.shoppinglist.Views.ShoppingListViews;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.abhishekaryan.shoppinglist.Entities.ShoppingList;
import com.example.abhishekaryan.shoppinglist.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ShoppingListViewHolder extends RecyclerView.ViewHolder {

    private TextView listName;
    private TextView createdBy;
    private TextView ownerName;
    private TextView listCreationDate;
    public View listShoppingView;
    public ShoppingListViewHolder(View itemView) {
        super(itemView);
        listName=(TextView)itemView.findViewById(R.id.list_shopping_list_name);
        createdBy=(TextView)itemView.findViewById(R.id.list_shopping_list_created_by);
        ownerName=(TextView)itemView.findViewById(R.id.list_shopping_list_owner_name);
        listCreationDate=(TextView)itemView.findViewById(R.id.list_shopping_list_creation_date);
        listShoppingView=itemView.findViewById(R.id.list_shopping_list_cardView);
    }

    public void listPopulate(ShoppingList shoppingList){

        listName.setText(shoppingList.getListName());
        ownerName.setText(shoppingList.getOwnerName());
        if(shoppingList.getDateListCreated().get("timestamp")!=null){
            listCreationDate.setText(convertTime((long)shoppingList.getDateListCreated().get("timestamp")));

        }

    }
    private String convertTime(Long unixTime){

        Date dateObject=new Date(unixTime);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("mm-dd-yy kk:mm");
        return simpleDateFormat.format(dateObject);
    }
}
