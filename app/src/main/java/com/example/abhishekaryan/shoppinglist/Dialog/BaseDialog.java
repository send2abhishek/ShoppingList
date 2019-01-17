package com.example.abhishekaryan.shoppinglist.Dialog;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.example.abhishekaryan.shoppinglist.Infrastructure.ShoppingListApplication;
import com.example.abhishekaryan.shoppinglist.Infrastructure.Utils;
import com.squareup.otto.Bus;

public class BaseDialog extends DialogFragment {

    protected Bus bus;
    protected ShoppingListApplication application;
    protected String UserEmail,UserName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application=(ShoppingListApplication)getActivity().getApplication();
        bus=application.getBus();

        SharedPreferences preferences=getActivity().getSharedPreferences(Utils.SHARED_PREFENCES,Context.MODE_PRIVATE);
        UserEmail=preferences.getString(Utils.EMAIL,"");
        UserName=preferences.getString(Utils.USERNAME,"");
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }
}
