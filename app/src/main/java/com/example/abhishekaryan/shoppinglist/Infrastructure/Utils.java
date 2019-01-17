package com.example.abhishekaryan.shoppinglist.Infrastructure;

public class Utils {

    public static final String FIREBASE_URL="https://shoopinglist-f55dc.firebaseio.com/";
    public static final String FIREBASE_USER_REFERNCE=FIREBASE_URL + "users/";
    public static final String FIREBASE_SHOPPING_LIST_REFERNCE=FIREBASE_URL + "userShoppingList/";
    public static final String FIREBASE_SHOPPING_LIST_ITEM_REFERNCE=FIREBASE_URL + "ShoppingListItems/";
    public static final String FIREBASE_ADD_FRND_REFERNCE=FIREBASE_URL + "usersFriends/";

    public static final String SHARED_PREFENCES="myPref";
    public static final String DIALOG_PREFENCES_DELETE="delPref";
    public static final String USERNAME="username";
    public static final String EMAIL="email";


    public static final String LIST_ORDER_PREFERENCES="LIST_ORDER_PREFERENCES";
    public static final String ORDER_BY_KEY="OrderByPushKey";


    public static String encodeEmail(String userEmail){

        return userEmail.replace(".",",");
    }
    public static String decodeEmail(String userEmail){

        return userEmail.replace(",",".");
    }




}
