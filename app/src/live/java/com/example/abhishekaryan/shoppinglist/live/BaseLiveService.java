package com.example.abhishekaryan.shoppinglist.live;

import com.example.abhishekaryan.shoppinglist.Infrastructure.ShoppingListApplication;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.otto.Bus;

public class BaseLiveService {

    protected Bus bus;
    protected ShoppingListApplication application;
    protected FirebaseAuth auth;

    public BaseLiveService(ShoppingListApplication application) {
        this.application = application;

        bus=application.getBus();
        bus.register(this);
        auth=FirebaseAuth.getInstance();



    }
}
