package com.example.abhishekaryan.shoppinglist.Infrastructure;

import android.app.Application;
import com.example.abhishekaryan.shoppinglist.live.Module;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.otto.Bus;

import java.math.BigDecimal;
import java.util.Currency;

public class ShoppingListApplication extends Application {

    private Bus bus;
    private DatabaseReference reference;


    public ShoppingListApplication() {
        bus=new Bus();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Module.register(this);
        reference=FirebaseDatabase.getInstance().getReference();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);



        //logger.logPurchase(BigDecimal.valueOf(4.32), Currency.getInstance("USD"));


    }

    public Bus getBus() {
        return bus;
    }
}
