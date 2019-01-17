package com.example.abhishekaryan.shoppinglist.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.abhishekaryan.shoppinglist.Entities.ShoppingList;
import com.example.abhishekaryan.shoppinglist.Services.ShoppingListService;

public class DeleteDialogFragment extends BaseDialog implements View.OnClickListener {

    public static final String EXTRA_SHOPPING_LIST_ID="EXTRA_SHOPPING_LIST_ID";
    public static final String EXTRA_BOOLEAN="EXTRA_BOOLEAN";
    private String mShoppingListId;
    private boolean misLongClicked;



    public static DeleteDialogFragment newInstance(ShoppingList ShoopingLIstId, boolean isLongClicked){

        Bundle bundle=new Bundle();



        bundle.putParcelable(EXTRA_SHOPPING_LIST_ID,ShoopingLIstId);
        bundle.putBoolean(EXTRA_BOOLEAN,isLongClicked);
        DeleteDialogFragment dialogFragment=new DeleteDialogFragment();
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ShoppingList list=getArguments().getParcelable(EXTRA_SHOPPING_LIST_ID);
        mShoppingListId=list.getId();
        misLongClicked=getArguments().getBoolean(EXTRA_BOOLEAN);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        AlertDialog dialog=new AlertDialog.Builder(getActivity())
                .setMessage("Are sure to delete")
                .setPositiveButton("Confirm",null)
                .setNegativeButton("Cancel",null)
                .show();

        dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(this);
        return dialog;

    }

    @Override
    public void onClick(View v) {

            if(misLongClicked){

                dismiss();

                bus.post(new ShoppingListService.DeleteShoppingListRequest(mShoppingListId,UserEmail));
                Toast.makeText(getActivity(),"Item deleted ",Toast.LENGTH_SHORT).show();

            }

            else {
                dismiss();
                getActivity().finish();
                bus.post(new ShoppingListService.DeleteShoppingListRequest(mShoppingListId,UserEmail));
                Toast.makeText(getActivity(),"Item deleted else block ",Toast.LENGTH_SHORT).show();
            }
    }
}
