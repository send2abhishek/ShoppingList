package com.example.abhishekaryan.shoppinglist.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.abhishekaryan.shoppinglist.R;
import com.example.abhishekaryan.shoppinglist.Services.ShoppingListService;
import com.squareup.otto.Subscribe;

public class AddDialogFragment extends BaseDialog implements View.OnClickListener {

    private EditText newListName;


    public static AddDialogFragment newInstance(){

        return new AddDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_add_list,null);

        newListName=(EditText)rootView.findViewById(R.id.dialog_add_list);
        AlertDialog alertDialog=new AlertDialog.Builder(getActivity())
                .setView(rootView)
                .setPositiveButton("Create",null)
                .setNegativeButton("Cancel",null)
                .show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(this);

        return alertDialog;
    }

    @Override
    public void onClick(View v) {
        bus.post(new ShoppingListService.AddShoppingListRequest(newListName.getText().toString(),UserName,UserEmail));
    }


    @Subscribe

    public void OnShoppingListAdded(ShoppingListService.AddShoppingListResponse response){

        if(!response.didSucceed()){

            newListName.setError(response.getPropertyErrors("listName"));
        }
        else {
            dismiss();
        }
    }
}
