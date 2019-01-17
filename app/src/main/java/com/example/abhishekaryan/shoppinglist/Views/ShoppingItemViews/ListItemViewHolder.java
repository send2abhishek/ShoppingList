package com.example.abhishekaryan.shoppinglist.Views.ShoppingItemViews;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhishekaryan.shoppinglist.Entities.Items;
import com.example.abhishekaryan.shoppinglist.R;

public class ListItemViewHolder extends RecyclerView.ViewHolder {

    public TextView newListItemName;
    public ImageView listItemItemView;
    private TextView boughtBy;
    private TextView boughtByText;
    public View listItemView;
    public ListItemViewHolder(View itemView) {
        super(itemView);

        newListItemName=(TextView)itemView.findViewById(R.id.list_shopping_list_add_name);
        listItemItemView=(ImageView)itemView.findViewById(R.id.list_shopping_list_item_view);
        boughtBy=(TextView)itemView.findViewById(R.id.list_shopping_list_add_owner_name);
        boughtByText=(TextView)itemView.findViewById(R.id.list_shopping_list_add_bought_by);
        listItemView=(View)itemView.findViewById(R.id.list_shopping_list_add_cardView);
    }


    public void Populate(Items items, Context context,String CurrentUserEmail){

        itemView.setTag(items);
        newListItemName.setText(items.getItemName());
        if(items.isBought()){
            newListItemName.setPaintFlags(newListItemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            listItemItemView.setImageResource(R.mipmap.ic_bought);
            boughtByText.setVisibility(View.VISIBLE);
            boughtBy.setVisibility(View.VISIBLE);


            if(CurrentUserEmail.equals(items.getBoughtBy())){

                boughtBy.setText("You Buddy !");
            }
            else {

                boughtBy.setText(items.getBoughtBy());

            }


        }
        else {
            boughtByText.setVisibility(View.GONE);
            boughtBy.setVisibility(View.GONE);
            listItemItemView.setImageResource(R.mipmap.ic_trash);
            newListItemName.setPaintFlags(newListItemName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

        }


    }
}
