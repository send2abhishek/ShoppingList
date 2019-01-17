package com.example.abhishekaryan.shoppinglist.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public class ShoppingList implements Parcelable {

    private String id;
    private String listName;
    private String ownerName;
    private String ownerEmail;
    private HashMap<String,Object> dateListCreated;

    //to keep track when list changed
    private HashMap<String,Object> dateListLastChanged;

    public ShoppingList() {
    }

    public ShoppingList(String id, String listName, String ownerName,
                        String ownerEmail, HashMap<String, Object> dateListCreated) {
        this.id = id;
        this.listName = listName;
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
        this.dateListCreated = dateListCreated;
        HashMap<String,Object> dateListLastChangedObject=new HashMap<>();
        dateListLastChangedObject.put("date",ServerValue.TIMESTAMP);

        //when list is created at that time, dateListCreated is equal to dateListLastChanged
        this.dateListLastChanged=dateListLastChangedObject;

    }

    protected ShoppingList(Parcel in) {
        id = in.readString();
        listName = in.readString();
        ownerName = in.readString();
        ownerEmail = in.readString();
    }

    public static final Creator<ShoppingList> CREATOR = new Creator<ShoppingList>() {
        @Override
        public ShoppingList createFromParcel(Parcel in) {
            return new ShoppingList(in);
        }

        @Override
        public ShoppingList[] newArray(int size) {
            return new ShoppingList[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getListName() {
        return listName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public HashMap<String, Object> getDateListCreated() {

        if(dateListLastChanged!=null){

            //means this class is instantiated by us
            return dateListCreated;
        }

        //Means if we are not going to create this shopping List
        HashMap<String,Object> dateListCreatedObject=new HashMap<>();
        dateListCreatedObject.put("date",ServerValue.TIMESTAMP);

        return dateListCreatedObject;
    }

    public HashMap<String, Object> getDateListLastChanged() {
        return dateListLastChanged;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(listName);
        dest.writeString(ownerName);
        dest.writeString(ownerEmail);
    }
}
