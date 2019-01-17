package com.example.abhishekaryan.shoppinglist.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.abhishekaryan.shoppinglist.Entities.ShoppingList;
import com.example.abhishekaryan.shoppinglist.R;
import com.example.abhishekaryan.shoppinglist.Services.ItemListService;
import com.squareup.otto.Subscribe;

public class AddDialogItemIntoList extends BaseDialog implements View.OnClickListener {

    public static final String SHOPPING_ADD_LIST_ITEM="SHOPPING_ADD_LIST_ITEM";
    private ShoppingList listItems;
    private EditText newItemInList;

    public static AddDialogItemIntoList newInstance(ShoppingList list){

        Bundle bundle=new Bundle();
        bundle.putParcelable(SHOPPING_ADD_LIST_ITEM,list);
        AddDialogItemIntoList addDialogItemIntoList=new  AddDialogItemIntoList();
        addDialogItemIntoList.setArguments(bundle);
        return addDialogItemIntoList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listItems=getArguments().getParcelable(SHOPPING_ADD_LIST_ITEM);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater=getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.add_items_to_shopping_list,null);
        newItemInList=(EditText)rootView.findViewById(R.id.dialog_add_list_items_shopping_item);


        AlertDialog dialog=new AlertDialog.Builder(getActivity())

                .setView(rootView)
                .setPositiveButton("Add Item",null)
                .setNegativeButton("Cancel",null)
                .show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(this);
        return dialog;
    }

    @Override
    public void onClick(View v) {


        bus.post(new ItemListService.AddItemRequest(listItems.getId(),
                newItemInList.getText().toString(),UserEmail));

    }


    @Subscribe

    public void OnListItemAdded(ItemListService.AddItemResponse response){

        if(!response.didSucceed()){

            newItemInList.setError(response.getPropertyErrors("error"));
        }

        dismiss();
    }
}
