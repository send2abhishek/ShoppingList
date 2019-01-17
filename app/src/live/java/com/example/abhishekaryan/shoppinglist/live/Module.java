package com.example.abhishekaryan.shoppinglist.live;

import com.example.abhishekaryan.shoppinglist.Infrastructure.ShoppingListApplication;

public class Module {

    public static void register(ShoppingListApplication application){

        new LiveAccountService(application);
        new LiveShoppingListService(application);
        new LiveAddItemService(application);
    }
}
