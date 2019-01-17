package com.example.abhishekaryan.shoppinglist.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.abhishekaryan.shoppinglist.Entities.Items;
import com.example.abhishekaryan.shoppinglist.Entities.ShoppingList;
import com.example.abhishekaryan.shoppinglist.Services.ItemListService;

public class RemoveDialogFragment extends BaseDialog {


    public static final String ITEMS_SHOPPING_LIST="ITEMS_SHOPPING_LIST";
    public static final String SHOPPING_LIST="SHOPPING_LIST";

    private String Itemid;
    private String ShoppingListId;



    public static RemoveDialogFragment newInstance(Items items, ShoppingList list){


        Bundle bundle=new Bundle();
        bundle.putParcelable(ITEMS_SHOPPING_LIST,items);
        bundle.putParcelable(SHOPPING_LIST,list);

        RemoveDialogFragment remove=new RemoveDialogFragment();
        remove.setArguments(bundle);

        return remove;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Items items=getArguments().getParcelable(ITEMS_SHOPPING_LIST);
        ShoppingList list=getArguments().getParcelable(SHOPPING_LIST);
        Itemid=items.getId();
        ShoppingListId=list.getId();

        AlertDialog deleteDialog =new AlertDialog.Builder(getActivity())

                .setMessage("Are you sure to delete")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bus.post(new ItemListService.RemoveItemRequest(Itemid,ShoppingListId,UserEmail));
                        dismiss();
                    }
                })
                .setNegativeButton("Cancel",null)

                .show();
        return deleteDialog;




    }








}
