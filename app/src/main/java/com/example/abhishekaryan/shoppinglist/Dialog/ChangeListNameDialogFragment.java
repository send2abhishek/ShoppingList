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
import com.example.abhishekaryan.shoppinglist.Services.ShoppingListService;
import com.squareup.otto.Subscribe;

public class ChangeListNameDialogFragment extends BaseDialog implements View.OnClickListener {


    public static final String SHOPPING_LIST_EXTRA_INFO="SHOPPING_LIST_EXTRA_INFO";
    private ShoppingList myList;
    private EditText NewShoppingListName;


    public static ChangeListNameDialogFragment newInstance(ShoppingList list){

        Bundle bundle=new Bundle();
        bundle.putParcelable(SHOPPING_LIST_EXTRA_INFO,list);
        ChangeListNameDialogFragment changeListNameDialogFragment=new ChangeListNameDialogFragment();
        changeListNameDialogFragment.setArguments(bundle);
        return changeListNameDialogFragment;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myList=getArguments().getParcelable(SHOPPING_LIST_EXTRA_INFO);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater=getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.list_details_add_list_dialog,null);
        NewShoppingListName=(EditText)rootView.findViewById(R.id.list_details_dialog_add_list);
        NewShoppingListName.setText(myList.getListName());

        AlertDialog dialog=new AlertDialog.Builder(getActivity())

                .setView(rootView)
                .setTitle("Change Shopping List Name ?")
                .setPositiveButton("ChangeName",null)
                .setNegativeButton("Cancel",null)
                .show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(this);
        return dialog;
    }

    @Override
    public void onClick(View v) {

        bus.post(new ShoppingListService.ChangeShoppingListRequest(NewShoppingListName.getText().toString(),
                myList.getId(),myList.getOwnerEmail()));

        dismiss();

    }

    @Subscribe
    public void onShoppingListChanged(ShoppingListService.ChangeShoppingListResponse response){

        if(!response.didSucceed()){

            NewShoppingListName.setError(response.getPropertyErrors("newName"));
        }

        Toast.makeText(getActivity(),"List will update soon buddy !",Toast.LENGTH_SHORT).show();
    }
}
